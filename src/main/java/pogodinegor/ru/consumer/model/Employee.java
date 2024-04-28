package pogodinegor.ru.consumer.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="employees")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employees_id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "address")
    private String address;

    @Column(name = "position")
    private String position;

    @ManyToMany
    @JoinTable(
            name = "employees_phones",
            joinColumns = @JoinColumn(name = "employees_id"),
            inverseJoinColumns = @JoinColumn(name = "phones_id"))
    private List<Phone> phones;
}
