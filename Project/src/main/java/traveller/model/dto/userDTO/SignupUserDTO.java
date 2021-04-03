package traveller.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;


@Setter
@Getter
@NoArgsConstructor
public class SignupUserDTO {
    @NotNull(message = "Name field is mandatory") //user - friendly message :)
    private String firstName;
    @NotNull(message = "Name field is mandatory")
    private String lastName;
    @Min(value = 2, message = "Age should not be less than 2")
    @Max(value = 110, message = "Age should not be greater than 110")
    private int age;
    @NotNull(message = "Please enter a username")
    private String username;
    @Email(message = "Please enter a valid email")
    private String email;
    private String password;
    private String repeatedPassword;
}
