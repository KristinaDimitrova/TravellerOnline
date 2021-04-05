package traveller.model.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import traveller.model.dto.user.OwnerDTO;
import traveller.model.pojo.Comment;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CommentResponseDTO implements Comparable<CommentResponseDTO> {
    private long id;
    OwnerDTO owner;
    private String text;
    @DateTimeFormat(pattern = "hh:mm dd/MM/yyyy")
    private LocalDateTime createdAt;
    private int likes;

    public CommentResponseDTO(Comment comment){
        owner = new OwnerDTO(comment.getOwner());
        text = comment.getText();
        createdAt = comment.getCreatedAt();
        likes = comment.getLikers().size();
    }

    @Override
    public int compareTo(CommentResponseDTO o) {
        if(this.createdAt.isEqual(o.createdAt) || this.createdAt.isBefore(o.createdAt)){
            return 1;
        }
        return 0;
    }


}
