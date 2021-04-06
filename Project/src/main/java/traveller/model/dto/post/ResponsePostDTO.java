package traveller.model.dto.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.dto.comment.CommentResponseDTO;
import traveller.model.dto.file.ImageDTO;
import traveller.model.dto.file.VideoDTO;
import traveller.model.dto.locationType.LocationTypeDTO;
import traveller.model.dto.user.OwnerDTO;
import traveller.model.dto.user.UserWithoutPasswordDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Image;
import traveller.model.pojo.Post;
import traveller.model.pojo.Video;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
public class ResponsePostDTO  {
    private long id;
    private OwnerDTO owner;
    private String latitude;
    private String longitude;
    private String description;
    private LocationTypeDTO locationType;
    private List<VideoDTO> videos = new ArrayList<>();
    private List<ImageDTO> images = new ArrayList<>();
    private List<CommentResponseDTO> comments = new ArrayList<>();
    private int likes;
    private int dislikes;

    public  ResponsePostDTO (Post post){
        this.id = post.getId();
        this.owner = new OwnerDTO(post.getOwner());
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.description = post.getDescription();
        this.locationType = new LocationTypeDTO(post.getLocationType());
        for(Video v : post.getVideos()){
            videos.add(new VideoDTO(v));
        }
        for(Image i : post.getImages()){
            images.add(new ImageDTO(i));
        }
        for(Comment c : post.getComments()){
            comments.add(new CommentResponseDTO(c));
        }
        this.likes = post.getLikers().size();
        this.dislikes = post.getDislikers().size();

    }


}