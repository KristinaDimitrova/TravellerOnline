package traveller.model.DTO.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SignupUserDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String repeatedPassword;

}
