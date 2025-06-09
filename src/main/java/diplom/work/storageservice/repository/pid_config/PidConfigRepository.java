package diplom.work.storageservice.repository.pid_config;

import diplom.work.storageservice.model.pid_config.PidConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PidConfigRepository extends JpaRepository<PidConfig, Long> {
    List<PidConfig> findByRoomIdOrderByIdDesc(Long roomId);

    Optional<PidConfig> findFirstByRoomIdAndActiveIsTrue(Long roomId);

    @Modifying
    @Query("UPDATE PidConfig p SET p.active = false WHERE p.room.id = :roomId")
    void deactivateAllForRoom(@Param("roomId") Long roomId);
}
