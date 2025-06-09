package diplom.work.storageservice.service.room;

import diplom.work.storageservice.model.room.Room;
import diplom.work.storageservice.model.user.User;
import diplom.work.storageservice.repository.room.RoomRepository;
import diplom.work.storageservice.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserService userService;

    @Transactional
    public Room create(Long userId, Room room) {
        var user = userService.findUserById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with ID " + userId + " not found."));
        room.setUser(user);
        return roomRepository.save(room);
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> findAllByUser(Long userId) {
        return roomRepository.findAllByUserId(userId);
    }

    public Optional<Room> findRoomById(Long id) {
        return roomRepository.findById(id);
    }

    @Transactional
    public Room updateRoom(Long id, Room updatedRoom) {
        return roomRepository.findById(id)
                .map(room -> {
                    Optional.ofNullable(updatedRoom.getName()).ifPresent(room::setName);
                    Optional.ofNullable(updatedRoom.getRoomParams()).ifPresent(room::setRoomParams);
                    return roomRepository.save(room);
                })
                .orElseThrow(() -> new EntityNotFoundException("Room with ID " + id + " not found."));
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }
}
