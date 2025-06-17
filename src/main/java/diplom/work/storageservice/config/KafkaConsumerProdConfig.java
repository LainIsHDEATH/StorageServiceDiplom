package diplom.work.storageservice.config;

import diplom.work.storageservice.dto.sensor_data.SensorDataDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerProdConfig {

    @Bean
    public Map<String, Object> consumerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "storage-group");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "diplom.work.storageservice.dto.sensor_data");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "diplom.work.storageservice.dto.sensor_data.SensorDataDTO");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false); //если надо отключить type headers
        return props;
    }

    @Bean
    public ConsumerFactory<String, SensorDataDTO> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<
            ConcurrentMessageListenerContainer<String, SensorDataDTO>
            > kafkaListenerContainerFactory(
            KafkaTemplate<String, Object> kafkaTemplate // <--- добавь сюда!
    ) {
        ConcurrentKafkaListenerContainerFactory<String, SensorDataDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate),
                new FixedBackOff(5_000L, 3)
        );
        errorHandler.addNotRetryableExceptions(DeserializationException.class);
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> pf
    ) {
        return new KafkaTemplate<>(pf);
    }
}
