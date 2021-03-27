package traveller.model.DTO.commentDTO;

import traveller.model.POJOs.Comment;
import traveller.model.POJOs.Post;
import traveller.model.POJOs.User;
import java.time.LocalDateTime;

public class CommentResponseDTO {

    private User owner;
    private Post post;
    private String text;
    private LocalDateTime createdAt;

    public CommentResponseDTO(Comment comment){
        owner = comment.getOwner();
        post = comment.getPost();
        text = comment.getText();
        createdAt = comment.getCreatedAt();
    }
}
