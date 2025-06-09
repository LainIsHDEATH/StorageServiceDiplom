package diplom.work.storageservice.service.pid_config;

import diplom.work.storageservice.dto.pid_config.PidConfigDTO;
import diplom.work.storageservice.model.ai_model.AiModel;
import diplom.work.storageservice.model.pid_config.PidConfig;
import diplom.work.storageservice.repository.pid_config.PidConfigRepository;
import diplom.work.storageservice.repository.room.RoomRepository;
import diplom.work.storageservice.service.room.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PidConfigService {
    private final PidConfigRepository pidConfigRepository;
    private final RoomService roomService;

    public List<PidConfig> findAllConfigs() {
        return pidConfigRepository.findAll();
    }

    /** Список всех конфигов комнаты */
    public List<PidConfig> findAllByRoom(Long roomId) {
        return pidConfigRepository.findByRoomIdOrderByIdDesc(roomId);
    }

    public Optional<PidConfig> findById(Long id) {
        return pidConfigRepository.findById(id);
    }

    /** Текущий активный конфиг */
    public Optional<PidConfig> getActive(Long roomId) {
        return pidConfigRepository.findFirstByRoomIdAndActiveIsTrue(roomId);
    }

    /** Создать новый конфиг вручную — деактивирует старые */
    @Transactional
    public PidConfig createManual(Long roomId, PidConfig pidConfig) {
        // Проверка, что комната существует
        var room = roomService.findRoomById(roomId).orElseThrow(
                () -> new EntityNotFoundException("Room with ID " + roomId + " not found."));

        // Дезактивируем старые
        pidConfigRepository.deactivateAllForRoom(roomId);

        // Сохраняем новый
        pidConfig.setRoom(room);
        pidConfig.setActive(true);

        return pidConfig;
    }

    /** Обновить существующий конфиг */
    @Transactional
    public PidConfig updateConfig(Long configId, PidConfig updatedPidConfig) {
        return pidConfigRepository.findById(configId)
                .map(config -> {
                    Optional.ofNullable(updatedPidConfig.getKp()).ifPresent(config::setKp);
                    Optional.ofNullable(updatedPidConfig.getKd()).ifPresent(config::setKd);
                    Optional.ofNullable(updatedPidConfig.getKi()).ifPresent(config::setKi);
                    Optional.ofNullable(updatedPidConfig.getTunedMethod()).ifPresent(config::setTunedMethod);
                    return pidConfigRepository.save(config);
                })
                .orElseThrow(() -> new EntityNotFoundException("Config with ID " + configId + " not found."));
    }

    /** Удалить конфиг */
    @Transactional
    public void deleteConfig(Long configId) {
        pidConfigRepository.deleteById(configId);
    }
}