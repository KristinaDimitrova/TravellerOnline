package traveller.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import traveller.exceptions.AuthorizationException;
import traveller.exceptions.BadRequestException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.commentDTO.CommentCreationRequestDto;
import traveller.model.dto.commentDTO.CommentEditRequestDTO;
import traveller.model.dto.commentDTO.CommentResponseDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.model.dao.comment.CommentDBDao;
import traveller.model.repository.CommentRepository;
import traveller.model.repository.PostRepository;
import traveller.model.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

@Service
@Component
public class CommentService {
    @Autowired
    private CommentRepository commentRep;
    @Autowired
    private PostService postServ;
    @Autowired
    private CommentDBDao commentDao;
    @Autowired
    private PostRepository postRepo;
    private PostRepository postRep;
    @Autowired
    private UserRepository userRep;

    public CommentResponseDTO getById(long id) {
        return new CommentResponseDTO(commentRep.getById(id));
    }

    public MessageDTO delete(long commentId, long actorId) {
        Comment comment = commentRep.getById(commentId); //comment exists
        User postOwner = comment.getPost().getOwner();  //post exists
        User commentOwner = comment.getOwner();  //comment owner exists, else -> cascade delete TODO
        if(actorId != commentOwner.getId() || actorId != postOwner.getId()) {
            throw new AuthorizationException("You are not authorized to delete the comment.");
        }
        commentRep.deleteCommentById(comment.getId());
        return new MessageDTO("You deleted the comment.");
    }

    public Set<CommentResponseDTO> getComments(long postId) {
        Post post = postRep.getPostById(postId); //post exists
        TreeSet<CommentResponseDTO> commentsDto = new TreeSet<>();
        for (Comment c : commentRep.findCommentsByPost_Id(postId)){
            commentsDto.add(new CommentResponseDTO(c));
        }
        return commentsDto;
    }

    public MessageDTO hitLike(long commentId, long actorId){
        Comment comment = commentRep.getById(commentId); //comment exists
        Post post = postRep.getPostById(comment.getPost().getId()); //post exists
        User actor = userRep.getById(actorId);
        if(comment.getLikers().contains(actor)){
            throw new BadRequestException("You have already liked this comment.");
        }
        comment.getLikers().add(userRep.getById(actorId));
        commentRep.save(comment);
        return new MessageDTO("Comment liked.");
    }

    public MessageDTO removeLike(long commentId, long actorId) {
        Comment comment = commentRep.getById(commentId); //comment exists
        Post post = postRep.getPostById(comment.getPost().getId()); //post has not been deleted
        User actor = userRep.getById(actorId);
        if(!comment.getLikers().contains(actor)){
            throw new BadRequestException("You haven't liked this comment.");
        }
        comment.getLikers().remove(userRep.getById(actorId));
        commentRep.save(comment);
        return new MessageDTO("Comment unliked.");
    }

    public CommentResponseDTO addComment(CommentCreationRequestDto commentDto, long actorId) {
        //validate input >0 characters <255 characters todo
        Comment comment = new Comment();
        comment.setPost(postRep.getPostById(commentDto.getPostId())); //post exists
        comment.setText(commentDto.getText());  //text is okay
        comment.setCreatedAt(LocalDateTime.now());
        comment.setOwner(userRep.getById(actorId));
        commentRep.save(comment);
        return new CommentResponseDTO(comment);
    }

    public CommentResponseDTO editComment(CommentEditRequestDTO commentDto, long actorId) {
        //TODO
        Comment comment = commentRep.getById(commentDto.getId()); //comment exists
        if(comment.getOwner().getId() != actorId){ //comment is written by the same person
            throw new BadRequestException("Sorry, on Travergy you can only edit your own comments.");
        }
        comment.setText(commentDto.getText());
        commentRep.save(comment);
        return new CommentResponseDTO(comment);
    }
}
