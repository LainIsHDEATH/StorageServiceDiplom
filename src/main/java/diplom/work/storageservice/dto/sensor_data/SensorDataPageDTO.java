package diplom.work.storageservice.dto.sensor_data;

import java.util.List;

public record SensorDataPageDTO(
        List<SensorDataDTO> data,   // сами точки
        int  page,                 // номер текущей страницы (0-based)
        int  size,                 // сколько элементов в этой странице
        int  totalPages,           // всего страниц
        long totalElements         // всего записей (если возвращаете Page)
) {}
