package diplom.work.storageservice.repository.ai_model;

import diplom.work.storageservice.model.ai_model.AiModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiModelRepository extends JpaRepository<AiModel, Long> {
    List<AiModel> findAllByRoomId(Long roomId);
}
