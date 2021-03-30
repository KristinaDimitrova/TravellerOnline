package traveller.model.dto.commentDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class CommentResponseDTO implements Comparable<CommentResponseDTO> {
    private String ownerComment;
    private long postId;
    private String text;
    @DateTimeFormat(pattern = "hh:mm dd/MM/yyyy")
    private LocalDateTime createdAt;
    private int likes;

    public CommentResponseDTO(Comment comment){
        ownerComment = comment.getOwner().getFirstName().concat(" ").concat(comment.getOwner().getLastName());
        postId = comment.getPost().getId();
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
