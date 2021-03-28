package traveller.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.pojo.User;

@NoArgsConstructor
@Setter
@Getter
@Component
public class SignUpUserResponseDTO {
    private int age;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    public SignUpUserResponseDTO(User user){
        age = user.getAge();
        username = user.getUsername();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
    }

}

