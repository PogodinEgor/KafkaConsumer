package pogodinegor.ru.consumer.service.kafkaserviceimpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pogodinegor.ru.consumer.dto.EmployeePhoneDTO;
import pogodinegor.ru.consumer.service.KafkaReceiver;
import pogodinegor.ru.consumer.service.KafkaService;

@Slf4j
@Service
public class KafkaReceiverEmployeePhone implements KafkaReceiver {
    private final reactor.kafka.receiver.KafkaReceiver<String, Object> receiver;
    private final KafkaService kafkaService;

    public KafkaReceiverEmployeePhone(@Qualifier("employeePhoneReceiver") reactor.kafka.receiver.KafkaReceiver<String, Object> receiver, KafkaService kafkaService) {
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
                    EmployeePhoneDTO employeePhoneDTO = gson.fromJson(record.value().toString(), EmployeePhoneDTO.class);
                    try {
                        kafkaService.handleEmployeePhone(employeePhoneDTO);
                        record.receiverOffset().acknowledge();
                    } catch (Exception e) {
                        log.warn("Ошибка при обработке сообщения: " + e.getMessage());
                    }
                });
    }
}
