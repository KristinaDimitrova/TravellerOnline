package traveller.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.dto.commentDTO.CommentResponseDTO;
import traveller.model.dto.fileDTO.ImageDTO;
import traveller.model.dto.fileDTO.VideoDTO;
import traveller.model.dto.locationTypeDTO.LocationTypeDTO;

import java.util.LinkedList;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
public class ResponsePostDTO {

    private String ownerUsername;
    private String latitude;
    private String longitude;
    private String description;
    private LocationTypeDTO locationType;
    private List<VideoDTO> videos = new LinkedList<>();
    private List<ImageDTO> images = new LinkedList<>();
    private List<CommentResponseDTO> comments = new LinkedList<>();
    private int likes;
    private int dislikes;

//    public ResponsePostDTO(Post post){
//        this.ownerUsername = post.getOwner().getUsername();
//        this.latitude = post.getLatitude();
//        this.longitude = post.getLongitude();
//        this.description = post.getDescription();
//        this.locationType = new LocationTypeDTO(post.getLocationType()).getName();
//        for(Video v : post.getVideos()) {
//            videos.add(new VideoDTO(v));
//        }
//        for(Image i : post.getImages()) {
//            images.add(new ImageDTO(i));
//        }
//        for(Comment c : post.getComments()){
//            comments.add(new CommentResponseDTO(c));
//        }
//        this.likes = post.getLikers().size();
//        this.dislikes = post.getDislikers().size();
//    }
//}
}