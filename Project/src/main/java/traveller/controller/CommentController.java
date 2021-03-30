package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.commentDTO.CommentCreationRequestDto;
import traveller.model.dto.commentDTO.CommentEditRequestDTO;
import traveller.model.dto.commentDTO.CommentResponseDTO;
import traveller.service.CommentService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@RestController
public class CommentController extends AbstractController {
    @Autowired
    private CommentService comService;
    @Autowired
    private SessionManager sessManager;

    @PutMapping(value="posts/{postId}/comments")
    public CommentResponseDTO commentPost(@RequestBody CommentCreationRequestDto commentDto,
                                          @PathVariable("postId") long postId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session); //user has logged in
        return comService.addComment(postId, commentDto, actorId);
    }

    @PutMapping(value="/comments/{id}")
    public CommentResponseDTO edit(HttpSession session, @PathVariable("id") long commentId, @RequestBody MessageDTO commentDto){
        long actorId = sessManager.authorizeLogin(session);
        return comService.editComment(commentId, commentDto, actorId);
    }

    @DeleteMapping(value="/comments/{id}")
    public MessageDTO delete(@PathVariable("id") long commentId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.delete(commentId, actorId);
    }

    @GetMapping(value="/comments/{id}")
    public CommentResponseDTO getById(@PathVariable("id") long commentId, HttpSession session){
        sessManager.authorizeLogin(session);
        return comService.getById(commentId);
    }

    @PostMapping(value="/comments/{id}/1")
    public MessageDTO like(@PathVariable("id") long commentId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.hitLike(commentId, actorId);
    }

    @PostMapping(value="/comments/{id}/0")
    public MessageDTO unlike(@PathVariable("id") long commentId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.removeLike(commentId, actorId);
    }

    @GetMapping(value="/posts/{id}/comments")
    public List<CommentResponseDTO> getCommentsByPostId(HttpSession session, @PathVariable("id") long postId){
        sessManager.authorizeLogin(session);
        return comService.getComments(postId);
        //List<CommentResponseDTO> comments
    }
}
