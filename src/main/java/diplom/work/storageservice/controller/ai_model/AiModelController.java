package diplom.work.storageservice.controller.ai_model;

import diplom.work.storageservice.dto.ai_model.AiModelDTO;
import diplom.work.storageservice.dto.ai_model.AiModelListResponseDTO;
import diplom.work.storageservice.model.ai_model.AiModel;
import diplom.work.storageservice.service.ai_model.AiModelService;
import diplom.work.storageservice.util.AiModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
public class AiModelController {

    private final AiModelService aiModelService;
    private final AiModelMapper aiModelMapper;

    @GetMapping()
    public ResponseEntity<AiModelListResponseDTO> getAllModels() {
        return ResponseEntity.status(200).body(
                new AiModelListResponseDTO(
                        aiModelService.findAllModels()
                                .stream()
                                .map(aiModelMapper::toDto)
                                .toList()));
    }

    @GetMapping("/room-models/{roomId}")
    public ResponseEntity<AiModelListResponseDTO> getAllConfigsByRoom(@PathVariable Long roomId) {
        return ResponseEntity.status(200).body(
                new AiModelListResponseDTO(
                        aiModelService.findAllByRoom(roomId)
                                .stream()
                                .map(aiModelMapper::toDto)
                                .toList()));
    }

    @PostMapping("/room-models/{roomId}")
    public ResponseEntity<AiModelDTO> createModel(@PathVariable Long roomId,
                                                  @RequestBody AiModelDTO aiModelDTO) {
        AiModel aiModel = aiModelMapper.toEntity(aiModelDTO);

        return ResponseEntity.status(201).body(
                aiModelMapper.toDto(
                        aiModelService.create(roomId, aiModel)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AiModelDTO> getAiModelById(@PathVariable Long id) {
        return aiModelService.findById(id)
                .map(aiModelMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{modelId}")
    public ResponseEntity<?> update(@PathVariable Long modelId,
                                    @RequestBody AiModelDTO aiModelDTO) {
        AiModel aiModel = aiModelMapper.toEntity(aiModelDTO);

        try {
            return ResponseEntity.ok(
                    aiModelMapper.toDto(
                            aiModelService.updateModel(modelId, aiModel)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{modelId}")
    public ResponseEntity<?> delete(@PathVariable Long modelId) {
        try {
            aiModelService.deleteModel(modelId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
