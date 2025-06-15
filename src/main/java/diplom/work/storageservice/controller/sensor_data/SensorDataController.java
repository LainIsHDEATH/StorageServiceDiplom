package diplom.work.storageservice.controller.sensor_data;

import diplom.work.storageservice.dto.sensor_data.SensorDataDTO;
import diplom.work.storageservice.dto.sensor_data.SensorDataListResponseDTO;
import diplom.work.storageservice.dto.sensor_data.SensorDataPageDTO;
import diplom.work.storageservice.model.sensor_data.SensorData;
import diplom.work.storageservice.service.sensor_data.SensorDataService;
import diplom.work.storageservice.util.SensorDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sensor-data")
@RequiredArgsConstructor
public class SensorDataController {

    private final SensorDataMapper sensorDataMapper;
    private final SensorDataService sensorDataService;

    // Получить все SensorData по simulationId
    @GetMapping("/simulation-data/all/{simulationId}")
    public ResponseEntity<SensorDataListResponseDTO> getBySimulationIdAllData(@PathVariable Long simulationId) {
        return ResponseEntity.status(200).body(
                new SensorDataListResponseDTO(
                        sensorDataService.getAllBySimulationId(simulationId)
                                .stream()
                                .map(sensorDataMapper::toDto)
                                .toList()));
    }

    @GetMapping("/simulation-data/{simulationId}")
    public ResponseEntity<?> getPage(
            @PathVariable Long simulationId,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "1000") int size) {

        Page<SensorData> p = sensorDataService.getPage(simulationId, page, size);

        return ResponseEntity.ok(
                Map.of(
                        "data",           p.getContent().stream()
                                .map(sensorDataMapper::toDto).toList(),
                        "page",           p.getNumber(),
                        "size",           p.getSize(),
                        "totalPages",     p.getTotalPages(),
                        "totalElements",  p.getTotalElements()
                ));
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
