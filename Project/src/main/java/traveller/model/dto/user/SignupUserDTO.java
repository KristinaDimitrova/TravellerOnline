package traveller.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class SignupUserDTO {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private int age;
    @NotNull
    private String username;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String repeatedPassword;
}
