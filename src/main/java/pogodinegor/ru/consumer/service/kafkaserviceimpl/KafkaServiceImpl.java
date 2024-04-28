package pogodinegor.ru.consumer.service.kafkaserviceimpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pogodinegor.ru.consumer.dto.EmployeeDTO;
import pogodinegor.ru.consumer.dto.EmployeePhoneDTO;
import pogodinegor.ru.consumer.dto.PhoneDTO;
import pogodinegor.ru.consumer.exception.EmployeeException;
import pogodinegor.ru.consumer.exception.EmployeePhoneException;
import pogodinegor.ru.consumer.exception.PhoneException;
import pogodinegor.ru.consumer.model.Employee;
import pogodinegor.ru.consumer.model.Phone;
import pogodinegor.ru.consumer.repository.EmployeeRepository;
import pogodinegor.ru.consumer.repository.PhoneRepository;
import pogodinegor.ru.consumer.service.KafkaService;


@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {
    private final EmployeeRepository employeeRepository;
    private final PhoneRepository phoneRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void handlePhone(PhoneDTO phoneDTO) {
        if (phoneDTO.getNumber() != null && !phoneDTO.getNumber().isEmpty()) {
            if (!phoneRepository.existsByNumber(phoneDTO.getNumber())) {
                phoneRepository.save(convertToPhone(phoneDTO));

            } else {
                log.warn("Данный номер уже сохранен.");
                throw new PhoneException("Данный номер уже сохранен.");
            }
        } else {
            log.warn("Номер не может быть пустым.");
            throw new PhoneException("Номер не может быть пустым.");
        }

    }

    @Transactional
    @Override
    public void handleEmployee(EmployeeDTO employeeDTO) {
        if (employeeDTO.getFullName() != null && !employeeDTO.getFullName().isEmpty()) {
            employeeRepository.save(convertToEmployee(employeeDTO));
        } else {
            log.warn("DTO сотрудника не содержит полного имени.");
            throw new EmployeeException("DTO сотрудника не содержит полного имени.");
        }
    }

    @Transactional
    @Override
    public void handleEmployeePhone(EmployeePhoneDTO employeePhoneDTO) {
        Employee employee = employeeRepository.findById(employeePhoneDTO.getEmployeeId())
                .orElseThrow(() -> new EmployeeException("Сотрудник не найден"));
        Phone phone = phoneRepository.findById(employeePhoneDTO.getPhoneId())
                .orElseThrow(() -> new PhoneException("Телефон не найден"));

        if (!employee.getPhones().contains(phone)) {
            employee.getPhones().add(phone);
            log.info("Телефон с ID '{}' успешно добавлен сотруднику с ID '{}'", phone.getId(), employee.getId());
        } else {
            log.warn("Связь между сотрудником с ID '{}' и телефоном с ID '{}' уже существует", employee.getId(), phone.getId());
            throw new EmployeePhoneException("Связь между сотрудником и телефоном уже существует");
        }
    }

    private Phone convertToPhone(PhoneDTO phoneDTO) {
        return modelMapper.map(phoneDTO, Phone.class);
    }

    private Employee convertToEmployee(EmployeeDTO employeeDTO) {
        return modelMapper.map(employeeDTO, Employee.class);
    }


}
