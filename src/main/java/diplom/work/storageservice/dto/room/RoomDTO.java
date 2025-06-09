package diplom.work.storageservice.dto.room;

import diplom.work.storageservice.model.room.RoomParams;

public record RoomDTO(
        Long id,
        String name,
        RoomParams roomParams
) {
}
