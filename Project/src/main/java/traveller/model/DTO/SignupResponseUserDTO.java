package traveller.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.POJOs.User;

@NoArgsConstructor
@Setter
@Getter
@Component
public class SignupResponseUserDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;

    public SignupResponseUserDTO (User user){
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
    }

}
