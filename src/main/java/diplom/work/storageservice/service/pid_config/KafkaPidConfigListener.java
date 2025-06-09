//package diplom.work.storageservice.service.pid_config;
//
//import diplom.work.storageservice.dto.pid_config.PidConfigDTO;
//import diplom.work.storageservice.dto.sensor_data.SensorDataDTO;
//import diplom.work.storageservice.model.sensor_data.SensorData;
//import diplom.work.storageservice.service.sensor_data.SensorDataService;
//import diplom.work.storageservice.util.PidConfigMapper;
//import diplom.work.storageservice.util.SensorDataMapper;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class KafkaPidConfigListener {
//
//    private final PidConfigService pidConfigService;
//    private final PidConfigMapper pidConfigMapper;
//
////    @KafkaListener(topics = "sensor-data-topic", groupId = "storage-group")
////    public void listenSensorData(Long roomId, PidConfigDTO pidConfigDTO) {
////        pidConfigService.createManual(roomId, pidConfigMapper.toEntity(pidConfigDTO));
////    }
//}
