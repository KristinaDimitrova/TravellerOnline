package traveller.model.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EditDetailsUserDTO { //username cannot be changed
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
