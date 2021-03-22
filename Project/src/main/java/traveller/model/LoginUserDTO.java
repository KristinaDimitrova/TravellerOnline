package traveller.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
