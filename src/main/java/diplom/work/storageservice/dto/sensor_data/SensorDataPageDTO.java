package diplom.work.storageservice.dto.sensor_data;

import java.util.List;

public record SensorDataPageDTO(
        List<SensorDataDTO> data,
        int  page,
        int  size,
        int  totalPages,
        long totalElements
) {}
