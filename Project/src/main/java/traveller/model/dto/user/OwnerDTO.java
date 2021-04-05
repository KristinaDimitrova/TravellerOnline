package traveller.model.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.pojo.User;

@Setter
@Getter
@NoArgsConstructor
public class OwnerDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String username;

    public OwnerDTO(User user){
        id = user.getId();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        username = user.getUsername();
    }


}
