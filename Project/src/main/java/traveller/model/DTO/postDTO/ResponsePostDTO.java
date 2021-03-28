package traveller.model.DTO.postDTO;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.commentDTO.CommentResponseDTO;
import traveller.model.DTO.fileDTO.ResponseImageDTO;
import traveller.model.DTO.locationTypeDTO.LocationTypeDTO;
import traveller.model.POJOs.Comment;
import traveller.model.POJOs.Image;
import traveller.model.POJOs.Post;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
public class ResponsePostDTO {

    private String ownerName;
    private String latitude;
    private String longitude;
    private String description;
    private LocationTypeDTO locationTypeDTO;
    private byte[] video;
    private List<ResponseImageDTO> images;
    private List<CommentResponseDTO> comments;
    private int likes;
    private int dislikes;

    public ResponsePostDTO(Post post){
        this.ownerName = post.getOwner().getUsername();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.description = post.getDescription();
        this.locationTypeDTO = new LocationTypeDTO(post.getLocationType());
        downloadVideo(post);
        for(Image i : post.getImages()){
            images.add(new ResponseImageDTO(i));
        }
        for(Comment c : post.getComments()){
            comments.add(new CommentResponseDTO(c));
        }
        this.likes = post.getLikers().size();
        this.dislikes = post.getDislikers().size();
    }

    private void downloadVideo(Post post){
        String url = post.getVideoUrl();
        File phyFile = new File(url);
        try {
            this.video = Files.readAllBytes(phyFile.toPath());
        } catch (IOException e) {
            throw new NotFoundException("Sorry, problem with video downloading!");
        }
    }


}
