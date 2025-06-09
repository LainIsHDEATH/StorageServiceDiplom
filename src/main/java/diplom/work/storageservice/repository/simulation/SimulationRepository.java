package diplom.work.storageservice.repository.simulation;

import diplom.work.storageservice.model.simulation.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {
    List<Simulation> findAllByRoomId(Long roomId);
}
