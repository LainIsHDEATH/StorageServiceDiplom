package diplom.work.storageservice.controller.pid_config;

import diplom.work.storageservice.dto.ai_model.AiModelDTO;
import diplom.work.storageservice.dto.pid_config.PidConfigDTO;
import diplom.work.storageservice.dto.pid_config.PidConfigListResponseDTO;
import diplom.work.storageservice.model.pid_config.PidConfig;
import diplom.work.storageservice.service.pid_config.PidConfigService;
import diplom.work.storageservice.util.PidConfigMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pid-configs")
@RequiredArgsConstructor
@Validated
public class PidConfigController {

    private final PidConfigMapper pidConfigMapper;
    private final PidConfigService pidConfigService;

    @GetMapping()
    public ResponseEntity<PidConfigListResponseDTO> getAllConfigs() {
        return ResponseEntity.status(200).body(
                new PidConfigListResponseDTO(
                        pidConfigService.findAllConfigs()
                                .stream()
                                .map(pidConfigMapper::toDto)
                                .toList()));
    }

    @GetMapping("/room-configs/{roomId}")
    public ResponseEntity<PidConfigListResponseDTO> getAllConfigsByRoom(@PathVariable Long roomId) {
        return ResponseEntity.status(200).body(
                new PidConfigListResponseDTO(
                        pidConfigService.findAllByRoom(roomId)
                                .stream()
                                .map(pidConfigMapper::toDto)
                                .toList()));
    }

    @GetMapping("/room-configs/{roomId}/active")
    public ResponseEntity<PidConfigDTO> getActive(@PathVariable Long roomId) {
        return pidConfigService.getActive(roomId)
                .map(pidConfigMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/room-configs/{roomId}")
    public ResponseEntity<PidConfigDTO> create(@PathVariable Long roomId,
                                               @RequestBody PidConfigDTO pidConfigDTO) {
        PidConfig pidConfig = pidConfigMapper.toEntity(pidConfigDTO);

        return ResponseEntity.status(201).body(
                pidConfigMapper.toDto(
                        pidConfigService.createManual(roomId, pidConfig)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PidConfigDTO> getConfigById(@PathVariable Long id) {
        return pidConfigService.findById(id)
                .map(pidConfigMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{configId}")
    public ResponseEntity<?> update(@PathVariable Long configId,
                                    @RequestBody PidConfigDTO pidConfigDTO) {
        PidConfig pidConfig = pidConfigMapper.toEntity(pidConfigDTO);

        try {
            return ResponseEntity.ok(
                    pidConfigMapper.toDto(
                            pidConfigService.updateConfig(configId, pidConfig)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{configId}")
    public ResponseEntity<?> delete(@PathVariable Long configId) {
        try {
            pidConfigService.deleteConfig(configId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
