package diplom.work.storageservice.service.sensor_data;

import diplom.work.storageservice.model.sensor_data.SensorData;
import diplom.work.storageservice.repository.sensor_data.SensorDataRepository;
import diplom.work.storageservice.service.simulation.SimulationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorDataService {
    private final SensorDataRepository sensorRepository;
    private final SimulationService simulationService;

    public SensorData save(Long simulationId, SensorData data) {
        var simulation = simulationService.findSimulationById(simulationId).orElseThrow(
                () -> new EntityNotFoundException("Simulation with ID " + simulationId + " not found."));
        data.setSimulation(simulation);
        return sensorRepository.save(data);
    }

    public SensorData getLastBySimulationId(Long simulationId) {
        return sensorRepository.findTopBySimulationIdOrderByTimestampDesc(simulationId);
    }

    public List<SensorData> getAllBySimulationId(Long simulationId) {
        return sensorRepository.findAllBySimulationId(simulationId);
    }

    public Page<SensorData> getPage(Long simId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return sensorRepository.findBySimulationId(simId, pageable);
    }

    @Transactional
    public void addBatch(List<SensorData> data) {
        sensorRepository.saveAll(data);
    }

    @Transactional
    public void addBatch(Long simulationId, List<SensorData> data) {
        var simulation = simulationService.findSimulationById(simulationId).orElseThrow(
                () -> new EntityNotFoundException("Simulation with ID " + simulationId + " not found."));
        List<SensorData> batchList = data.stream()
                .peek(sensor -> sensor.setSimulation(simulation))
                .toList();
        sensorRepository.saveAll(batchList);
    }
}
