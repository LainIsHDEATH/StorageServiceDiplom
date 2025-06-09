package diplom.work.storageservice.util;

import diplom.work.storageservice.dto.room.RoomDTO;
import diplom.work.storageservice.model.room.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    @Mapping(target = "id", ignore = true)
    Room toEntity(RoomDTO roomDTO);

    RoomDTO toDto(Room room);
}
