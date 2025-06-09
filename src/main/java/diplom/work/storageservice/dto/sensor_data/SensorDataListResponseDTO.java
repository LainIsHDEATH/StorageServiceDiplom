package diplom.work.storageservice.dto.sensor_data;

import diplom.work.storageservice.dto.simulation.SimulationDTO;

import java.util.List;

public record SensorDataListResponseDTO(List<SensorDataDTO> sensorDataList) {
}
