package diplom.work.storageservice.dto.sensor_data;

import java.time.LocalDateTime;

public record SensorDataDTO(
        Long simulationId,
        LocalDateTime timestamp,
        Double tempIn,
        Double tempOut,
        Double tempSetpoint,
        Double heaterPower,
        Double predictedTemp,
        Boolean isWindowOpen,
        Boolean isDoorOpen,
        Integer peopleCount
) {
}
