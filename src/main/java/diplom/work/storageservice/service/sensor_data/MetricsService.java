package diplom.work.storageservice.service.sensor_data;

import diplom.work.storageservice.dto.sensor_data.MetricsDTO;
import diplom.work.storageservice.model.sensor_data.SensorData;
import diplom.work.storageservice.repository.sensor_data.SensorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final SensorDataRepository repo;

    public MetricsDTO calc(long simulationId) {

        List<SensorData> rows =
                repo.findAllBySimulationIdOrderByTimestampAsc(simulationId);

        if (rows.isEmpty()) return new MetricsDTO(0,0,0,0,0, 0);

        /* ------- подготовка ------- */
        // шаг симуляции берём из двух первых отметок
        double dt = Duration.between(
                rows.get(0).getTimestamp(),
                rows.get(1).getTimestamp()
        ).toSeconds();                         // в секундах

        /* ------- аккумуляторы ------- */
        double absErrSum = 0;          // для MAE
        double sqErrSum  = 0;          // для MSE / RMSE
        double joules    = 0;          // энергия, Дж
        double maxOvershoot = 0;       // максимальный OS

        double EPS = 0.5;                   // допуск ±0.5 °C
        double settlingTime = -1;           // якщо не знайдемо – повернемо -1
        double worstSettling = 0;   // найдовший час

        boolean inTransient = false;        // йде перехід?
        int    t0Index      = 0;            // індекс початку переходу

        /* ------- переменные сегмента переходного процесса ------- */
        double currentSet = rows.get(0).getTempSetpoint();
        double segMaxTemp = rows.get(0).getTempIn();

        for (int i = 0; i < rows.size(); i++) {
            SensorData s = rows.get(i);

            /* --- виявили зміну уставки --- */
            if (i > 0 && s.getTempSetpoint() != rows.get(i - 1).getTempSetpoint()) {
                inTransient = true;
                t0Index     = i;            // початок нового переходу
            }

            /* --- якщо у фазі врегулювання --- */
            if (inTransient) {
                // перевіряємо: помилка останнього зразка <= EPS?
                double err = Math.abs(s.getTempIn() - s.getTempSetpoint());
                if (err > EPS) continue;    // ще не втрималися у смузі

                // тепер дивимося, чи ВСІ наступні зразки в межах ±EPS
                boolean allInside = true;
                for (int j = i; j < rows.size(); j++) {
                    double e = Math.abs(rows.get(j).getTempIn()
                            - rows.get(j).getTempSetpoint());
                    if (e > EPS) { allInside = false; break; }
                }
                if (allInside) {
                    Duration d = Duration.between(rows.get(t0Index).getTimestamp(),
                            s.getTimestamp());
                    worstSettling = Math.max(worstSettling, d.toSeconds());
                    inTransient = false;       // дозволяємо ловити наступний перехід
                }
            }

            /* --- 1.   ошибки --- */
            double err = s.getTempIn() - s.getTempSetpoint();
            absErrSum += Math.abs(err);       // Σ|e|  :contentReference[oaicite:10]{index=10}
            sqErrSum  += err * err;           // Σe²   :contentReference[oaicite:11]{index=11}

            /* --- 2.   энергия --- */
            // P (Вт) * dt (с) = Дж; 1 кВт·ч = 3 600 000 Дж.
            joules += s.getHeaterPower() * dt;   // (2.13) :contentReference[oaicite:12]{index=12}

            /* --- 3.   перерегулирование --- */
            // если уставка сменилась – закрываем прошлый сегмент
            if (Double.compare(s.getTempSetpoint(), currentSet) != 0) {
                maxOvershoot = Math.max(maxOvershoot,
                        Math.max(0, segMaxTemp - currentSet)); // (2.16) :contentReference[oaicite:13]{index=13}
                // начать новый сегмент
                currentSet = s.getTempSetpoint();
                segMaxTemp = s.getTempIn();
            } else {
                segMaxTemp = Math.max(segMaxTemp, s.getTempIn());
            }
        }
        // закрываем последний сегмент
        maxOvershoot = Math.max(maxOvershoot,
                Math.max(0, segMaxTemp - currentSet));

        /* ------- итоговые формулы ------- */
        int n = rows.size();
        double mae  = absErrSum / n;                // (2.14)
        double mse  = sqErrSum  / n;                // (2.15)
        double rmse = Math.sqrt(mse);               // (2.15)
        double kWh  = joules / 3_600_000;           // привели к кВт·ч

        return new MetricsDTO(mae, mse, rmse, kWh, maxOvershoot, worstSettling);
    }
}
