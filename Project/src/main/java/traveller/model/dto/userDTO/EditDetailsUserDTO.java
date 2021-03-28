package traveller.model.dto.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.pojo.User;

@NoArgsConstructor
@Getter
@Setter
public class EditDetailsUserDTO { //username cannot be changed
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
