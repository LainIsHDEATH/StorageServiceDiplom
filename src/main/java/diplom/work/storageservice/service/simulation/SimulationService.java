package diplom.work.storageservice.service.simulation;

import diplom.work.storageservice.model.simulation.Simulation;
import diplom.work.storageservice.repository.simulation.SimulationRepository;
import diplom.work.storageservice.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimulationService {
    private final SimulationRepository simulationRepository;
    private final RoomService roomService;

    @Transactional
    public Simulation create(Long roomId, Simulation simulation) {
        var room = roomService.findRoomById(roomId).orElseThrow(
                () -> new EntityNotFoundException("Room with ID " + roomId + " not found."));
        simulation.setRoom(room);
        return simulationRepository.save(simulation);
    }

    public List<Simulation> findAllSimulations() {
        return simulationRepository.findAll();
    }

    public List<Simulation> findAllByRoom(Long roomId) {
        return simulationRepository.findAllByRoomId(roomId);
    }

    public Optional<Simulation> findSimulationById(Long id) {
        return simulationRepository.findById(id);
    }

    @Transactional
    public Simulation updateSimulation(Long id, Simulation updatedSimulation) {
        return simulationRepository.findById(id)
                .map(simulation -> {
                    Optional.ofNullable(updatedSimulation.getControllerType()).ifPresent(simulation::setControllerType);
                    Optional.ofNullable(updatedSimulation.getStatus()).ifPresent(simulation::setStatus);
                    return simulationRepository.save(simulation);
                })
                .orElseThrow(() -> new EntityNotFoundException("Simulation with ID " + id + " not found."));
    }

    @Transactional
    public Simulation setStatus(long id, Simulation.Status status) {
        if (simulationRepository.updateStatus(id, status) == 0) {
            throw new EntityNotFoundException("Simulation " + id + " not found");
        }
        return simulationRepository.findById(id).orElseThrow();
    }

    @Transactional
    public void deleteSimulation(Long simulationId) {
        simulationRepository.deleteById(simulationId);
    }
}
