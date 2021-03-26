package traveller.model.DTO.userDTO;
import traveller.model.POJOs.User;

public class UserWithoutPasswordDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;

    public UserWithoutPasswordDTO(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        username = user.getUsername();
    }
}
