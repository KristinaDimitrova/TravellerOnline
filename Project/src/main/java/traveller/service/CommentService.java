package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import traveller.exception.AuthorizationException;
import traveller.exception.BadRequestException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.commentDTO.CommentCreationRequestDto;
import traveller.model.dto.commentDTO.CommentResponseDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.repository.CommentRepository;
import traveller.repository.PostRepository;
import traveller.repository.UserRepository;
import traveller.utilities.Validator;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class CommentService {
    @Autowired
    private CommentRepository commentRep;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private UserRepository userRep;

    public CommentResponseDTO getById(long id) {
        return new CommentResponseDTO(commentRep.getById(id));
    }

    @Transactional
    public MessageDTO delete(long commentId, long actorId) {
        Comment comment = commentRep.getById(commentId); //comment exists
        User postOwner = comment.getPost().getOwner();  //post exists
        User commentOwner = comment.getOwner();  //comment owner exists, else
        if(actorId != commentOwner.getId() || actorId != postOwner.getId()) {
            throw new AuthorizationException("You are not authorized to delete the comment.");
        }
        commentRep.deleteCommentById(commentId);
        return new MessageDTO("You deleted the comment.");
    }

    public List<CommentResponseDTO> getComments(long postId) {
        Post existingPost = postRepo.getPostById(postId);
        List<Comment> comments = existingPost.getComments(); //fixme get from database => ordered
        List<CommentResponseDTO> ordered = new ArrayList<>();
        for (Comment c : comments){
            ordered.add(new CommentResponseDTO(c));
        }
        ordered.forEach(comment -> System.out.println(comment.getText()));
        return ordered;
    }

    public MessageDTO hitLike(long commentId, long actorId){
        Comment comment = commentRep.getById(commentId);
        Post post = postRepo.getPostById(comment.getPost().getId());
        User actor = userRep.getById(actorId);
        if(comment.getLikers().contains(actor)){
            throw new BadRequestException("You have already liked this comment.");
        }
        comment.getLikers().add(userRep.getById(actorId));
        commentRep.save(comment);
        return new MessageDTO("Comment liked.");
    }

    public MessageDTO removeLike(long commentId, long actorId) {
        Comment comment = commentRep.getById(commentId);
        Post post = postRepo.getPostById(comment.getPost().getId()); //post has not been deleted
        User actor = userRep.getById(actorId);
        if(!comment.getLikers().contains(actor)){
            throw new BadRequestException("You haven't liked this comment.");
        }
        comment.getLikers().remove(userRep.getById(actorId));
        commentRep.save(comment);
        return new MessageDTO("Comment unliked.");
    }

    public CommentResponseDTO addComment(long postId, CommentCreationRequestDto commentDto, long actorId) {
        Validator.validateComment(commentDto.getText());
        Comment comment = new Comment(commentDto);
        comment.setPost(postRepo.getPostById(postId)); //post exists
        comment.setOwner(userRep.getById(actorId));
        commentRep.save(comment);
        return new CommentResponseDTO(comment);
    }

    public CommentResponseDTO editComment(long commentId, MessageDTO commentDto, long actorId) {
        Comment comment = commentRep.getById(commentId);
        if(comment.getOwner().getId() != actorId){ //comment is written by the same person
            throw new BadRequestException("Sorry, on Travergy you can only edit your own comments.");
        }
        comment.setText(commentDto.getMessage());
        return new CommentResponseDTO(commentRep.save(comment));
    }
}
