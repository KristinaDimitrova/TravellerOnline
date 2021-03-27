package traveller.model.DTO.userDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.POJOs.User;

@NoArgsConstructor
@Getter
@Setter
public class EditDetailsUserDTO { //username cannot be changed
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public EditDetailsUserDTO(User user) {
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        password = user.getPassword();
    }
}
