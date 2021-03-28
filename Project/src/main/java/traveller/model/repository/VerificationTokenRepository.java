package traveller.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import traveller.model.pojo.User;
import traveller.utilities.VerificationToken;
@Repository
@Component
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Integer> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);
}
