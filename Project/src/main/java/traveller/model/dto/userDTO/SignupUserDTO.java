package traveller.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Setter
@Getter
@NoArgsConstructor
public class SignupUserDTO {
    private String firstName;
    private String lastName;
    private int age;
    private String username;
    @NotBlank(message = "Finally it works.")
    @Email(message = "Bum shaka laka")
    private String email;
    private String password;
    private String repeatedPassword;

}
