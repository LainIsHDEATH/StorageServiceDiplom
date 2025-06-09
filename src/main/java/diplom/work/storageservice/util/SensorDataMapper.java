package diplom.work.storageservice.util;

import diplom.work.storageservice.dto.sensor_data.SensorDataDTO;
import diplom.work.storageservice.model.sensor_data.SensorData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SensorDataMapper {
    @Mapping(target = "id", ignore = true)
    SensorData toEntity(SensorDataDTO dto);

    SensorDataDTO toDto(SensorData entity);
}