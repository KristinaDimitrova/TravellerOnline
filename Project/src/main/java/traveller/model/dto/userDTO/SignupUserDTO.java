package traveller.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Setter
@Getter
@NoArgsConstructor
public class SignupUserDTO {
    @NotNull(message = "Name cannot be null")
    private String firstName;
    @NotNull(message = "Name cannot be null")
    private String lastName;
    @Min(value = 18, message = "Age should not be less than 2")
    @Max(value = 150, message = "Age should not be greater than 110")
    private int age;
    @NotNull(message = "Name cannot be null")
    private String username;
    @Email(message = "Email should be valid")
    private String email;
    private String password;
    private String repeatedPassword;



}
