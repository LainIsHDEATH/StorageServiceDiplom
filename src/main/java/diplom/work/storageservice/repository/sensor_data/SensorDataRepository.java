package diplom.work.storageservice.repository.sensor_data;

import diplom.work.storageservice.model.sensor_data.SensorData;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findAllBySimulationId(Long simulationId);

    SensorData findTopBySimulationIdOrderByTimestampDesc(Long simulationId);

    Page<SensorData> findBySimulationId(
            Long simulationId,
            Pageable pageable);

    List<SensorData> findAllBySimulationIdOrderByTimestampAsc(Long id);

//    @Query("SELECT SUM(s.heaterPower) * 15.0 / (3600.0) " +
//            "FROM SensorData s " +
//            "WHERE s.roomName = :roomName AND s.id BETWEEN :startId AND :endId")
//    Double getConsumedEnergyKWh(@Param("roomName") String roomName,
//                                @Param("startId") Long startId,
//                                @Param("endId") Long endId);

}
