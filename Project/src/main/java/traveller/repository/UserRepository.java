package traveller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import traveller.exception.NotFoundException;
import traveller.model.pojo.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Component
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);

    default User getById(long id){
        Optional<User> user = findById(id);
        if(user.isPresent()){
            return user.get();
        }else{
            throw new NotFoundException("User not found");
        }
    }

    List<User> findByFirstNameAndLastName(String firstName, String lastName);

    Optional<User> findById(Long id);

    void deleteUserById(long id);

    void delete(User user);

    User save(User user);

}
