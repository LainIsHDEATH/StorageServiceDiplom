package diplom.work.storageservice.controller.sensor_data;

import diplom.work.storageservice.dto.sensor_data.MetricsDTO;
import diplom.work.storageservice.service.sensor_data.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulations")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metrics;

    @GetMapping("/{id}/metrics")
    public MetricsDTO metrics(@PathVariable long id) {
        return metrics.calc(id);
    }
}
