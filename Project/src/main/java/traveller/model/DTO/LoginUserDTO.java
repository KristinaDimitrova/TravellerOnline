package traveller.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginUserDTO {
    //user can login using email or password
    private String email;
    private String username;
    private String password;
}