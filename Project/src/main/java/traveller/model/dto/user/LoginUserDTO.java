package traveller.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class LoginUserDTO {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
