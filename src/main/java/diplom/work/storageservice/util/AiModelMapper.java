package diplom.work.storageservice.util;

import diplom.work.storageservice.dto.ai_model.AiModelDTO;
import diplom.work.storageservice.model.ai_model.AiModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AiModelMapper {
    @Mapping(target = "id",   ignore = true)
    AiModel toEntity(AiModelDTO dto);

    AiModelDTO toDto(AiModel entity);
}
