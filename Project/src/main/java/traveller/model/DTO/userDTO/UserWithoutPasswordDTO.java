package traveller.model.DTO.userDTO;
import traveller.model.POJOs.Post;
import traveller.model.POJOs.User;

import java.util.List;

public class UserWithoutPasswordDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private List<Post> posts;

    public UserWithoutPasswordDTO(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        username = user.getUsername();
        for(Post p : user.getPosts()){
            //posts.add(new PostResponseDto(p))
        }

    }
}
