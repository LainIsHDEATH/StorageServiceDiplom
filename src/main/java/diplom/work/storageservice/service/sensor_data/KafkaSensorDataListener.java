package diplom.work.storageservice.service.sensor_data;

import diplom.work.storageservice.dto.sensor_data.SensorDataDTO;
import diplom.work.storageservice.model.sensor_data.SensorData;
import diplom.work.storageservice.util.SensorDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KafkaSensorDataListener {

    private final SensorDataService sensorDataService;
    private final SensorDataMapper sensorDataMapper;
    private final List<SensorDataDTO> buffer = Collections.synchronizedList(new ArrayList<>());

    private void addToBuffer(SensorDataDTO data) {
        buffer.add(data);
    }

    @Scheduled(fixedDelay = 5_000)  // запустить через 5 секунд после предыдущего окончания
    private void flushBuffer() {
        List<SensorDataDTO> toSave;
        synchronized (buffer) {
            if (buffer.isEmpty()) {
                return;
            }
            toSave = new ArrayList<>(buffer);
            buffer.clear();
        }
        // сохраняем пачкой
        // группируем по simulationId
        Map<Long, List<SensorDataDTO>> grouped = toSave.stream()
                .collect(Collectors.groupingBy(SensorDataDTO::simulationId));

        // Для каждой группы вызываем batch save
        grouped.forEach((simulationId, group) -> {
            List<SensorData> batch = group.stream()
                    .map(sensorDataMapper::toEntity)
                    .toList();
            sensorDataService.addBatch(simulationId, batch); // если нужно simulationId, передай сюда
        });
    }

    @KafkaListener(topics = "simulation-data-topic", groupId = "storage-group", containerFactory = "kafkaListenerContainerFactory")
    public void listenSensorData(SensorDataDTO data, Acknowledgment ack) {
        try {
            addToBuffer(data);
            ack.acknowledge();   // подтверждаем, что сообщение обработано успешно
        } catch (Exception ex) {
            // можно залогировать и пробросить, тогда errorHandler его поймает
            throw ex;
        }
    }
}
