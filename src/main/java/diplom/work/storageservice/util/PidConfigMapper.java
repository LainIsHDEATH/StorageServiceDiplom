package diplom.work.storageservice.util;

import diplom.work.storageservice.dto.pid_config.PidConfigDTO;
import diplom.work.storageservice.model.pid_config.PidConfig;
import diplom.work.storageservice.model.room.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PidConfigMapper {
    @Mapping(target="id",        ignore=true)
    @Mapping(target="active",  ignore=true) // по умолчанию true
    PidConfig toEntity(PidConfigDTO dto);

    PidConfigDTO toDto(PidConfig entity);
}
