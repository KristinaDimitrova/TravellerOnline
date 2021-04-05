package traveller.model.dto.user;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.dto.post.ResponsePostDTO;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserWithoutPasswordDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private int age;
    @JsonManagedReference
    private List<ResponsePostDTO> posts = new ArrayList<>();

    public UserWithoutPasswordDTO(User user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.age = user.getAge();
        for(Post p : user.getPosts()){
            posts.add(new ResponsePostDTO(p));
        }

    }


}
