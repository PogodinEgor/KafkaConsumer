package pogodinegor.ru.consumer.service;

import pogodinegor.ru.consumer.dto.EmployeeDTO;
import pogodinegor.ru.consumer.dto.EmployeePhoneDTO;
import pogodinegor.ru.consumer.dto.PhoneDTO;

public interface KafkaService {
    void handlePhone(PhoneDTO phoneDTO);
    void handleEmployee(EmployeeDTO employeeDTO);
    void handleEmployeePhone(EmployeePhoneDTO employeePhoneDTO);
}
