package pogodinegor.ru.consumer.config;

import com.jcabi.xml.XML;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    private final XML settings;

    @Bean
    public Map<String, Object> receiverProperties(){
        Map<String, Object> props = new HashMap<>(5);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                new TextXPath(
                        this.settings, "//groupId"
                ).toString()
        );
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                new TextXPath(
                        this.settings, "//keyDeserializer"
                ).toString()
        );
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                new TextXPath(
                        this.settings, "//valueDeserializer"
                ).toString()
        );
        props.put(
                "spring.json.trusted.packages",
                new TextXPath(
                        this.settings, "//trustedPackages"
                ).toString()
        );

        return props;
    }

@Bean
public ReceiverOptions<String, Object> employeeReceiverOptions() {
    return ReceiverOptions.<String, Object>create(receiverProperties())
            .subscription(Collections.singletonList("employee-lists"));
}

    @Bean
    public ReceiverOptions<String, Object> phoneReceiverOptions() {
        return ReceiverOptions.<String, Object>create(receiverProperties())
                .subscription(Collections.singletonList("phone-lists"));
    }

    @Bean
    public ReceiverOptions<String, Object> employeePhoneReceiverOptions() {
        return ReceiverOptions.<String, Object>create(receiverProperties())
                .subscription(Collections.singletonList("employeePhoneLink-lists"));
    }

    @Bean
    public KafkaReceiver<String, Object> employeeReceiver(ReceiverOptions<String, Object> employeeReceiverOptions) {
        return KafkaReceiver.create(employeeReceiverOptions);
    }

    @Bean
    public KafkaReceiver<String, Object> phoneReceiver(ReceiverOptions<String, Object> phoneReceiverOptions) {
        return KafkaReceiver.create(phoneReceiverOptions);
    }

    @Bean
    public KafkaReceiver<String, Object> employeePhoneReceiver(ReceiverOptions<String, Object> employeePhoneReceiverOptions) {
        return KafkaReceiver.create(employeePhoneReceiverOptions);
    }
}
