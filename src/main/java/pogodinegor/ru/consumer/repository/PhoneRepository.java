package pogodinegor.ru.consumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pogodinegor.ru.consumer.model.Phone;

import java.util.Optional;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    Optional<Phone> findById(Long id);
    boolean existsByNumber(String number);
}
