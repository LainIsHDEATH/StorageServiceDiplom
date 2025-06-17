package diplom.work.storageservice.service.sensor_data;

import diplom.work.storageservice.dto.sensor_data.MetricsDTO;
import diplom.work.storageservice.model.sensor_data.SensorData;
import diplom.work.storageservice.repository.sensor_data.SensorDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MetricsService {

    private final SensorDataRepository repo;

    public MetricsDTO calc(long simulationId) {

        List<SensorData> rows =
                repo.findAllBySimulationIdOrderByTimestampAsc(simulationId);
        double dtSeconds = Duration.between(
                rows.get(0).getTimestamp(),
                rows.get(1).getTimestamp()
        ).toSeconds();

        List<Double> actualTemps = new ArrayList<>();
        List<Double> setpointTemps = new ArrayList<>();
        List<Double> powersKw = new ArrayList<>();

        for (SensorData row : rows) {
            actualTemps.add(row.getTempIn());
            setpointTemps.add(row.getTempSetpoint());
            powersKw.add(row.getHeaterPower() / 1000.0);
        }

        int n = actualTemps.size();
        if (setpointTemps.size() != n || powersKw.size() != n)
            throw new IllegalArgumentException("Усі списки мають бути однакової довжини");
        if (n == 0)
            throw new IllegalArgumentException("Порожні вибірки");

        double absSum = 0.0, sqSum = 0.0;
        for (int i = 0; i < n; i++) {
            double diff = actualTemps.get(i) - setpointTemps.get(i);
            absSum += Math.abs(diff);
            sqSum += diff * diff;
        }
        double mae = absSum / n;
        double mse = sqSum / n;
        double rmse = Math.sqrt(mse);

        double dtHours = dtSeconds / 3600.0;
        double energyKWh = 0.0;
        for (double p : powersKw) {
            energyKWh += p * dtHours;
        }

        double initialSp = setpointTemps.get(0);
        int stepIndex = 0;        // t0
        double newSetpoint = initialSp;

        for (int i = 1; i < n; i++) {
            if (!Objects.equals(setpointTemps.get(i), initialSp)) {
                stepIndex = i;
                newSetpoint = setpointTemps.get(i);
                break;
            }
        }
        double tolerance = 0.02 * newSetpoint;
        double maxTemp = actualTemps.stream()
                .skip(stepIndex)
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(newSetpoint);
        double overshoot = Math.max(0.0, maxTemp - newSetpoint);

        double settlingTimeS;
        {
            int settlingIndex = -1;
            outer:
            for (int i = stepIndex; i < n; i++) {
                if (Math.abs(actualTemps.get(i) - newSetpoint) > tolerance)
                    continue;

                for (int j = i + 1; j < n; j++) {
                    if (Math.abs(actualTemps.get(j) - newSetpoint) > tolerance) {
                        continue outer;
                    }
                }
                settlingIndex = i;
                break;
            }

            if (settlingIndex < 0) {
                settlingTimeS = -1.0;
            } else {
                settlingTimeS = (settlingIndex - stepIndex) * dtSeconds;
            }
        }

        return new MetricsDTO(mae, mse, rmse, energyKWh, overshoot, settlingTimeS);
    }
}
