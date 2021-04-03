package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.commentDTO.CommentRequestDTO;
import traveller.model.dto.commentDTO.CommentResponseDTO;
import traveller.service.CommentService;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


@RestController
public class CommentController extends AbstractController {
    @Autowired
    private CommentService comService;
    @Autowired
    private SessionManager sessManager;

    @PutMapping(value="posts/{postId}/comments")
    public CommentResponseDTO commentPost(@RequestBody @Valid CommentRequestDTO commentDto,
                                          @PathVariable("postId") long postId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session); //user has logged in
        return comService.addComment(postId, commentDto, actorId);
    }

    @PutMapping(value="/comments/{id}")
    public CommentResponseDTO edit(HttpSession session, @PathVariable("id") long commentId, @RequestBody @Valid CommentRequestDTO commentDto){
        long actorId = sessManager.authorizeLogin(session);
        return comService.editComment(commentId, commentDto, actorId);
    }

    @DeleteMapping(value="/comments/{id}")
    public MessageDTO delete(@PathVariable("id") long commentId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.delete(commentId, actorId);
    }

    @GetMapping(value="/comments/{id}")
    public CommentResponseDTO getById(@PathVariable("id") long id, HttpSession session){
        sessManager.authorizeLogin(session);
        return comService.getById(id);
    }

    @PostMapping(value="/comments/{id}/1")
    public CommentResponseDTO like(@PathVariable("id") long commentId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.hitLike(commentId, actorId);
    }

    @PostMapping(value="/comments/{id}/0")
    public CommentResponseDTO unlike(@PathVariable("id") long commentId, HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.removeLike(commentId, actorId);
    }

    @GetMapping(value="/posts/{id}/comments")
    public List<CommentResponseDTO> getCommentsByPostId(HttpSession session, @PathVariable("id") long postId){
        sessManager.authorizeLogin(session);
        return comService.getComments(postId);
    }
}
