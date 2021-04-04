package traveller.model.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.dto.comment.CommentResponseDTO;
import traveller.model.dto.file.ImageDTO;
import traveller.model.dto.file.VideoDTO;
import traveller.model.dto.locationType.LocationTypeDTO;
import traveller.model.dto.user.OwnerDTO;


import java.util.LinkedList;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
public class ResponsePostDTO {
    private OwnerDTO owner;
    private String latitude;
    private String longitude;
    private String description;
    private LocationTypeDTO locationType;
    private List<VideoDTO> videos = new LinkedList<>();
    private List<ImageDTO> images = new LinkedList<>();
    private List<CommentResponseDTO> comments = new LinkedList<>();
    private int likes;
    private int dislikes;

}