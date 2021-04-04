package traveller.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import traveller.exception.AuthorizationException;
import traveller.exception.BadRequestException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.comment.CommentRequestDTO;
import traveller.model.dto.comment.CommentResponseDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.repository.CommentRepository;
import traveller.repository.PostRepository;
import traveller.repository.UserRepository;
import traveller.utilities.Validator;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class CommentService {
    @Autowired
    private CommentRepository commentRepo;
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private UserRepository userRep;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private  UserService userService;

    public CommentResponseDTO getById(long id) {
        return convertToCommentResponseDto(commentRepo.getById(id));
    }

    @Transactional
    public MessageDTO delete(long commentId, long actorId) {
        Comment comment = commentRepo.getById(commentId); //comment exists
        User postOwner = comment.getPost().getOwner();  //post exists
        User commentOwner = comment.getOwner();  //comment owner exists, else
        if(actorId != commentOwner.getId() || actorId != postOwner.getId()) {
            throw new AuthorizationException("You are not authorized to delete the comment.");
        }
        commentRepo.deleteCommentById(commentId);
        return new MessageDTO("You deleted the comment.");
    }

    public List<CommentResponseDTO> getComments(long postId) {
        Post existingPost = postRepo.getPostById(postId);
        List<Comment> comments = existingPost.getComments();
        List<CommentResponseDTO> ordered = new ArrayList<>();
        for (Comment c : comments){
            ordered.add(convertToCommentResponseDto(c));
        }
        return ordered;
    }

    public CommentResponseDTO hitLike(long commentId, long actorId){
        Comment comment = commentRepo.getById(commentId);
        postRepo.getPostById(comment.getPost().getId()); // does post exist
        User actor = userRep.getById(actorId);
        if(comment.getLikers().contains(actor)){
            throw new BadRequestException("You have already liked this comment.");
        }
        comment.getLikers().add(userRep.getById(actorId));
        commentRepo.save(comment);
        return convertToCommentResponseDto(commentRepo.getById(commentId));
    }

    public CommentResponseDTO removeLike(long commentId, long actorId) {
        Comment comment = commentRepo.getById(commentId);
        postRepo.getPostById(comment.getPost().getId()); //post has not been deleted
        User actor = userRep.getById(actorId);
        if(!comment.getLikers().contains(actor)){
            throw new BadRequestException("You haven't liked this comment.");
        }
        comment.getLikers().remove(userRep.getById(actorId));
        commentRepo.save(comment);
        return convertToCommentResponseDto(commentRepo.getById(commentId));
    }

    public CommentResponseDTO addComment(long postId, CommentRequestDTO commentDto, long actorId) {
        Validator.validateComment(commentDto.getText());
        Comment comment = convertCommentDtoToEntity(commentDto);
        comment.setPost(postRepo.getPostById(postId)); //post exists
        comment.setOwner(userRep.getById(actorId));

        comment.setCreatedAt(LocalDateTime.now());
        commentRepo.save(comment);
        return convertToCommentResponseDto(comment);
    }

    public CommentResponseDTO editComment(long commentId, CommentRequestDTO commentDto, long actorId) {
        Comment comment = commentRepo.getById(commentId);
        if(comment.getOwner().getId() != actorId){ //comment is written by the same person
            throw new BadRequestException("Sorry, on Travergy you can only edit your own comments.");
        }
        comment.setText(commentDto.getText());
        return convertToCommentResponseDto(commentRepo.save(comment)) ;

    }

    public CommentResponseDTO convertToCommentResponseDto(Comment comment) {
        CommentResponseDTO commentResponseDTO = modelMapper.map(comment, CommentResponseDTO.class);
        commentResponseDTO.setOwner(userService.convertUserEntityToOwnerDto(comment.getOwner()));
        return  commentResponseDTO;
    }

    public Comment convertCommentDtoToEntity(CommentRequestDTO commentDTO)   { ;
        return  modelMapper.map(commentDTO, Comment.class);
    }
}
