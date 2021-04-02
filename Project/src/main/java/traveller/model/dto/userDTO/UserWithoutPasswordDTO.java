package traveller.model.dto.userDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.dto.postDTO.ResponsePostDTO;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class UserWithoutPasswordDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private int age;
    @JsonManagedReference
    private List<ResponsePostDTO> posts = new ArrayList<>();


}
