package traveller.model.dto.commentDTO;

import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import java.time.LocalDateTime;

public class CommentResponseDTO implements Comparable<CommentResponseDTO> {
    private User owner;
    private Post post;
    private String text;
    private LocalDateTime createdAt;
    private int likes;

    public CommentResponseDTO(Comment comment){
        owner = comment.getOwner();
        post = comment.getPost();
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
