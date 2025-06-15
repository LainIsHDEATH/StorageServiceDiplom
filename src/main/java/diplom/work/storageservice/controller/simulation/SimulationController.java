package diplom.work.storageservice.controller.simulation;

import diplom.work.storageservice.dto.room.RoomDTO;
import diplom.work.storageservice.dto.room.RoomListResponseDTO;
import diplom.work.storageservice.dto.simulation.SimulationDTO;
import diplom.work.storageservice.dto.simulation.SimulationListResponseDTO;
import diplom.work.storageservice.model.room.Room;
import diplom.work.storageservice.model.simulation.Simulation;
import diplom.work.storageservice.service.room.RoomService;
import diplom.work.storageservice.service.simulation.SimulationService;
import diplom.work.storageservice.util.RoomMapper;
import diplom.work.storageservice.util.SimulationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulations")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationMapper simulationMapper;
    private final SimulationService simulationService;

    @PostMapping("/{roomId}")
    public ResponseEntity<SimulationDTO> saveSimulation(@PathVariable Long roomId,
                                                        @RequestBody SimulationDTO simulationDTO) {
        Simulation simulation = simulationMapper.toEntity(simulationDTO);

        return ResponseEntity.status(201).body(
                simulationMapper.toDto(
                        simulationService.create(roomId, simulation)));
    }

    @GetMapping()
    public ResponseEntity<SimulationListResponseDTO> getAllSimulations() {
        return ResponseEntity.status(200).body(
                new SimulationListResponseDTO(
                        simulationService.findAllSimulations()
                                .stream()
                                .map(simulationMapper::toDto)
                                .toList()));
    }

    @GetMapping("/room-simulations/{roomId}")
    public ResponseEntity<SimulationListResponseDTO> getAllSimulationsByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.status(200).body(
                new SimulationListResponseDTO(
                        simulationService.findAllByRoom(roomId)
                                .stream()
                                .map(simulationMapper::toDto)
                                .toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SimulationDTO> getSimulationById(@PathVariable Long id) {
        return simulationService.findSimulationById(id)
                .map(simulationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSimulation(@PathVariable Long id,
                                        @RequestBody SimulationDTO simulationDTO) {
        Simulation simulationDetails = simulationMapper.toEntity(simulationDTO);

        try {
            return ResponseEntity.ok(
                    simulationMapper.toDto(
                            simulationService.updateSimulation(id, simulationDetails)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/running/{id}")
    public ResponseEntity<SimulationDTO> markRunning(@PathVariable Long id) {
        Simulation sim = simulationService.setStatus(id, Simulation.Status.RUNNING);
        return ResponseEntity.ok(SimulationDTO.fromEntity(sim));
    }

    @PutMapping("/finished/{id}")
    public ResponseEntity<SimulationDTO> markFinished(@PathVariable Long id) {
        Simulation sim = simulationService.setStatus(id, Simulation.Status.FINISHED);
        return ResponseEntity.ok(SimulationDTO.fromEntity(sim));
    }

    @PutMapping("/failed/{id}")
    public ResponseEntity<SimulationDTO> markFailed(@PathVariable Long id) {
        Simulation sim = simulationService.setStatus(id, Simulation.Status.FAILED);
        return ResponseEntity.ok(SimulationDTO.fromEntity(sim));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSimulation(@PathVariable Long id) {
        try {
            simulationService.deleteSimulation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
