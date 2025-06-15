package diplom.work.storageservice.controller.room;

import diplom.work.storageservice.dto.room.RoomDTO;
import diplom.work.storageservice.dto.room.RoomListResponseDTO;
import diplom.work.storageservice.model.room.Room;
import diplom.work.storageservice.service.room.RoomService;
import diplom.work.storageservice.util.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomMapper roomMapper;
    private final RoomService roomService;

    @PostMapping("/{userId}")
    public ResponseEntity<RoomDTO> saveRoom(@PathVariable Long userId,
                                            @RequestBody RoomDTO roomDTO) {
        System.out.println(roomDTO.toString());
        Room room = roomMapper.toEntity(roomDTO);

        return ResponseEntity.status(201).body(
                roomMapper.toDto(
                        roomService.create(userId, room)));
    }

    @GetMapping()
    public ResponseEntity<RoomListResponseDTO> getAllRooms() {
        return ResponseEntity.status(200).body(
                new RoomListResponseDTO(
                        roomService.findAllRooms()
                                .stream()
                                .map(roomMapper::toDto)
                                .toList()));
    }

    @GetMapping("/user-rooms/{userId}")
    public ResponseEntity<RoomListResponseDTO> getAllRoomsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(200).body(
                new RoomListResponseDTO(
                        roomService.findAllByUser(userId)
                                .stream()
                                .map(roomMapper::toDto)
                                .toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        return roomService.findRoomById(id)
                .map(roomMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id,
                                        @RequestBody RoomDTO roomDTO) {
        Room roomDetails = roomMapper.toEntity(roomDTO);

        try {
            return ResponseEntity.ok(
                    roomMapper.toDto(
                            roomService.updateRoom(id, roomDetails)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        try {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
