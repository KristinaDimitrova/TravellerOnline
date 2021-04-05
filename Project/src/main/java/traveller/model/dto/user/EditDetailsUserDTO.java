package traveller.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EditDetailsUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
