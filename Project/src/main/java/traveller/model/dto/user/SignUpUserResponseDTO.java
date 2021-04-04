package traveller.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

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

//    public SignUpUserResponseDTO(User user){
//        age = user.getAge();
//        username = user.getUsername();
//        email = user.getEmail();
//        firstName = user.getFirstName();
//        lastName = user.getLastName();
//    }

    //todo mapper

}

