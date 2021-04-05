package traveller.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignupUserDTO {
    private String firstName;
    private String lastName;
    private int age;
    private String username;
    private String email;
    private String password;
    private String repeatedPassword;
}
