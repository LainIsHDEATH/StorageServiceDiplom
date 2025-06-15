package diplom.work.storageservice.dto.ai_model;

public record AiModelDTO(
        Long id,
        String type,
        String path,
        String description,
        Boolean active
) {
}