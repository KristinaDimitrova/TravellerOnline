package traveller.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;


@Setter
@Getter
@NoArgsConstructor
public class SignupUserDTO {
    private String firstName;
    private String lastName;
    private int age;

    private String username;
    @Email(message = "invalid email")
    private String email;
    private String password;
    private String repeatedPassword;

}
