package diplom.work.storageservice.controller.sensor_data;

import diplom.work.storageservice.dto.sensor_data.SensorDataDTO;
import diplom.work.storageservice.dto.sensor_data.SensorDataListResponseDTO;
import diplom.work.storageservice.model.sensor_data.SensorData;
import diplom.work.storageservice.service.sensor_data.SensorDataService;
import diplom.work.storageservice.util.SensorDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensor-data")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataMapper sensorDataMapper;
    private final SensorDataService sensorDataService;

    // Получить все SensorData по simulationId
    @GetMapping("/simulation-data/{simulationId}")
    public ResponseEntity<SensorDataListResponseDTO> getBySimulationId(@PathVariable Long simulationId) {
        return ResponseEntity.status(200).body(
                new SensorDataListResponseDTO(
                        sensorDataService.getAllBySimulationId(simulationId)
                                .stream()
                                .map(sensorDataMapper::toDto)
                                .toList()));
    }

    @GetMapping("/simulation-data/{simulationId}/last")
    public ResponseEntity<SensorDataDTO> getLastBySimulationId(@PathVariable Long simulationId) {
        return ResponseEntity.status(200).body(
                sensorDataMapper.toDto(
                        sensorDataService.getLastBySimulationId(simulationId)));
    }

    // Добавить шаг симуляции
    @PostMapping("/simulation-data/{simulationId}/step")
    public ResponseEntity<SensorDataDTO> addStep(@PathVariable Long simulationId,
                                                 @RequestBody SensorDataDTO sensorDataDTO) {
        SensorData sensorData = sensorDataMapper.toEntity(sensorDataDTO);

        return ResponseEntity.status(201).body(
                sensorDataMapper.toDto(
                        sensorDataService.save(simulationId, sensorData)));
    }

    // Batch insert (несколько записей)
    @PostMapping("/simulation-data/{simulationId}/batch")
    public ResponseEntity<Void> addBatch(@PathVariable Long simulationId,
                                         @RequestBody List<SensorDataDTO> sensorDataDtoList) {
        List<SensorData> sensorData = sensorDataDtoList.stream()
                .map(sensorDataMapper::toEntity)
                .toList();

        sensorDataService.addBatch(simulationId, sensorData);

        return ResponseEntity.status(201).build();
    }
}
