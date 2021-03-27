package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.DTO.MessageDTO;
import traveller.model.DTO.commentDTO.CommentCreationRequestDto;
import traveller.model.DTO.commentDTO.CommentEditRequestDTO;
import traveller.model.DTO.commentDTO.CommentResponseDTO;
import traveller.model.services.CommentService;

import javax.servlet.http.HttpSession;
import javax.xml.stream.events.Comment; //delete
import java.util.List;

@RestController
public class CommentController extends AbstractController { //todo Moni
    @Autowired
    private CommentService comService;
    @Autowired
    private SessionManager sessManager;

    //create comment todo
    @PostMapping(value="posts/{postId}/comments")
    public CommentResponseDTO commentPost(@PathVariable("postId") long postId, @RequestParam("text") String text,
                               HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.addComment(new CommentCreationRequestDto(text, postId, actorId));
        //if userId exists
    }


    //edit comment todo
    @PutMapping(value="/comments/{id}")
    public CommentResponseDTO edit(HttpSession session, @PathVariable("id") long id, @RequestBody CommentEditRequestDTO commentReqDto){
        long actorId = sessManager.authorizeLogin(session);

        return null;
    }

    @DeleteMapping(value="/comments/{id}")
    public MessageDTO delete(@PathVariable("id") long commentId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.delete(commentId, actorId);
    }

    @GetMapping(value="/comments/{id}")
    public CommentResponseDTO getById(@PathVariable("id") long id, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.getById(id);
    }

    //like todo
    @PostMapping(value="/comments/{id}/1")
    public CommentResponseDTO like(@PathVariable("id") long commentId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.hitLike(commentId, actorId);
    }

    //unlike todo
    @PostMapping(value="/comments/{id}/0")
    public CommentResponseDTO unlike(@PathVariable("id") long commentId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.removeLike(commentId, actorId);
    }

    @GetMapping(value="/posts/{id}/comments") //postman todo
    public List<CommentResponseDTO> getCommentsByPostId(HttpSession session, @PathVariable("id") long postId){
        long actorId = sessManager.authorizeLogin(session);
        return comService.getComments(postId);
    }
}
