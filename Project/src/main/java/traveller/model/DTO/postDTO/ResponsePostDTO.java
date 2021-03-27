package traveller.model.DTO.postDTO;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.POJOs.Comment;
import traveller.model.POJOs.Image;
import traveller.model.POJOs.User;

import javax.persistence.OneToMany;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
public class ResponsePostDTO {

    private User owner;
    private String latitude;
    private String longitude;
    private String description;
  //  private Video video;
    private List<Image> images;
    private List<Comment> comments;

}
