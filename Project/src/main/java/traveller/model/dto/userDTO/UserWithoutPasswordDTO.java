package traveller.model.dto.userDTO;
import traveller.model.dto.postDTO.ResponsePostDTO;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;

import java.util.List;

public class UserWithoutPasswordDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private int age;
    private List<ResponsePostDTO> posts;

    public UserWithoutPasswordDTO(User user) {
        firstName = user.getFirstName();
        lastName = user.getLastName();
        email = user.getEmail();
        username = user.getUsername();
        for(Post p : user.getPosts()){
            posts.add(new ResponsePostDTO(p));
        }
        age = user.getAge();
    }
}
