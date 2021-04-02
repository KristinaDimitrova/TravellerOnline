package traveller.model.dto.postDTO;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.dto.commentDTO.CommentResponseDTO;
import traveller.model.dto.fileDTO.ImageDTO;
import traveller.model.dto.fileDTO.VideoDTO;
import traveller.model.dto.locationTypeDTO.LocationTypeDTO;
import traveller.model.dto.userDTO.OwnerDTO;


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