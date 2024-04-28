package pogodinegor.ru.consumer.service.kafkaserviceimpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pogodinegor.ru.consumer.dto.PhoneDTO;
import pogodinegor.ru.consumer.service.KafkaReceiver;
import pogodinegor.ru.consumer.service.KafkaService;

@Slf4j
@Service
public class KafkaReceiverPhone implements KafkaReceiver {
    private final reactor.kafka.receiver.KafkaReceiver<String, Object> receiver;
    private final KafkaService kafkaService;

    public KafkaReceiverPhone(@Qualifier("phoneReceiver") reactor.kafka.receiver.KafkaReceiver<String, Object> receiver, KafkaService kafkaService) {
        this.receiver = receiver;
        this.kafkaService = kafkaService;
    }

    @PostConstruct
    private void init() {
        fetch();

    }

    @Override
    public void fetch() {
        Gson gson = new GsonBuilder().create();
        receiver.receive()
                .subscribe(record -> {
                    PhoneDTO phoneDTO = gson.fromJson(record.value().toString(), PhoneDTO.class);
                    try {
                        kafkaService.handlePhone(phoneDTO);
                        record.receiverOffset().acknowledge();
                    } catch (Exception e) {
                        log.warn("Ошибка при обработке сообщения: ");
                    }
                });
    }

}
