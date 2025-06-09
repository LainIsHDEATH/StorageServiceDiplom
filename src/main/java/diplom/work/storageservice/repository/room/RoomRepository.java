package diplom.work.storageservice.repository.room;

import diplom.work.storageservice.model.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByUserId(Long userId);
}
