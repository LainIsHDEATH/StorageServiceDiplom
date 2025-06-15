package diplom.work.storageservice.repository.simulation;

import diplom.work.storageservice.model.simulation.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {
    List<Simulation> findAllByRoomId(Long roomId);

    @Modifying
    @Query("""
           update Simulation s set s.status = :status
           where s.id = :id
           """)
    int updateStatus(@Param("id") Long id, @Param("status") Simulation.Status status);
}
