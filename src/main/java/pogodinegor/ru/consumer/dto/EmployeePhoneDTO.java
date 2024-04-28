package pogodinegor.ru.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeePhoneDTO {
    private Long employeeId;
    private Long phoneId;
}
