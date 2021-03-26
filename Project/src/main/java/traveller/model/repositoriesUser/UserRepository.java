package traveller.model.repositoriesUser;

import org.springframework.data.jpa.repository.JpaRepository;
import traveller.exceptions.NotFoundException;
import traveller.model.POJOs.User;
import java.util.List;
import java.util.Optional;

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

    void deleteUserById(Long id);

}