package diplom.work.storageservice.dto.pid_config;

public record PidConfigDTO(
        Long   id,
        Double kp,
        Double ki,
        Double kd,
        String tunedMethod,
        Boolean active
) {
}
