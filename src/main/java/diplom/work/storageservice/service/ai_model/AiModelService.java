package diplom.work.storageservice.service.ai_model;

import diplom.work.storageservice.dto.ai_model.AiModelDTO;
import diplom.work.storageservice.model.ai_model.AiModel;
import diplom.work.storageservice.model.pid_config.PidConfig;
import diplom.work.storageservice.model.room.Room;
import diplom.work.storageservice.repository.ai_model.AiModelRepository;
import diplom.work.storageservice.repository.room.RoomRepository;
import diplom.work.storageservice.service.room.RoomService;
import diplom.work.storageservice.util.AiModelMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiModelService {
    private final AiModelRepository aiModelRepository;
    private final RoomService roomService;

    @Transactional
    public AiModel create(Long roomId, AiModel aiModel) {
        Room room = roomService.findRoomById(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room with id " + roomId + " not found."));
        aiModel.setRoom(room);
        return aiModelRepository.save(aiModel);
    }

    public List<AiModel> findAllModels() {
        return aiModelRepository.findAll();
    }

    public List<AiModel> findAllByRoom(Long roomId) {
        return aiModelRepository.findAllByRoomId(roomId);
    }

    public Optional<AiModel> findById(Long id) {
        return aiModelRepository.findById(id);
    }

    @Transactional
    public AiModel updateModel(Long modelId, AiModel updatedAiModel) {
        return aiModelRepository.findById(modelId)
                .map(aiModel -> {
                    Optional.ofNullable(updatedAiModel.getType()).ifPresent(aiModel::setType);
                    Optional.ofNullable(updatedAiModel.getPath()).ifPresent(aiModel::setPath);
                    Optional.ofNullable(updatedAiModel.getDescription()).ifPresent(aiModel::setDescription);
                    return aiModelRepository.save(aiModel);
                })
                .orElseThrow(() -> new EntityNotFoundException("AiModel with ID " + modelId + " not found"));
    }

    @Transactional
    public void deleteModel(Long modelId) {
        aiModelRepository.deleteById(modelId);
    }
}
