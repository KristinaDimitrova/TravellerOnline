package traveller.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class EditDetailsUserDTO {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String password;

}
