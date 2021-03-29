package traveller.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.exception.NotFoundException;
import traveller.model.dto.commentDTO.CommentResponseDTO;
import traveller.model.dto.fileDTO.ResponseImageDTO;
import traveller.model.dto.locationTypeDTO.LocationTypeDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Image;
import traveller.model.pojo.Post;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
    private String locationType;
    private byte[] video;
    private List<ResponseImageDTO> images ;
    private List<CommentResponseDTO> comments;
    private int likes;
    private int dislikes;

    public ResponsePostDTO(Post post){
        this.ownerName = post.getOwner().getUsername();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.description = post.getDescription();
        this.locationType = new LocationTypeDTO(post.getLocationType()).getName();
        if(post.getVideoUrl()!=null){
            downloadVideo(post);
        }
        this.images = new ArrayList<>();
        if(post.getImages() != null){
            for(Image i : post.getImages()){
                images.add(new ResponseImageDTO(i));
            }
        }
        this.comments = new ArrayList<>();
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
