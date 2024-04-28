package pogodinegor.ru.consumer.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "phones")
@Data
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phones_id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "type")
    private String type;

    @ManyToMany(mappedBy = "phones",cascade = CascadeType.MERGE)
    private List<Employee> employees;
}
