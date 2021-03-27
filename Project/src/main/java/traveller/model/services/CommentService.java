package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import traveller.exceptions.AuthorizationException;
import traveller.model.DTO.MessageDTO;
import traveller.model.DTO.commentDTO.CommentCreationRequestDto;
import traveller.model.DTO.commentDTO.CommentResponseDTO;
import traveller.model.POJOs.Comment;
import traveller.model.POJOs.Post;
import traveller.model.POJOs.User;
import traveller.model.dao.comment.CommentDBDao;
import traveller.model.repositories.CommentRepository;
import traveller.model.repositories.PostRepository;
import traveller.model.repositories.UserRepository;
import traveller.utilities.Validate;

import java.util.ArrayList;
import java.util.List;

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

    public List<CommentResponseDTO> getComments(long postId) {
        Post post = postRepo.getPostById(postId); //post exists
        List<CommentResponseDTO> commentsDto = new ArrayList<>();
        for (Comment c : commentRep.findCommentsByPost_Id(postId)){
            commentsDto.add(new CommentResponseDTO(c));
        }
        return commentsDto;
    }

    public CommentResponseDTO hitLike(long commentId, long actorId){
        //does post exist?
        //does comment exist? TODO
        //is there a like?
        return null;
    }

    public CommentResponseDTO removeLike(long commentId, long actorId) {
        //does post exist?
        //does comment exist? TODO
        //is there a like?
        return null;
    }

    public CommentResponseDTO addComment(CommentCreationRequestDto commentDto) {
        //validate stuff
        postServ.getPostById(commentDto.getPostId()); //post exists
        Validate.comment(commentDto.getText());
        return null; //TODO
    }
}
