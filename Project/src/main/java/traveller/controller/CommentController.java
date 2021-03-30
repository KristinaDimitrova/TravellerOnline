package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.commentDTO.CommentCreationRequestDto;
import traveller.model.dto.commentDTO.CommentEditRequestDTO;
import traveller.model.dto.commentDTO.CommentResponseDTO;
import traveller.service.CommentService;

import javax.servlet.http.HttpSession;
import java.util.Set;

@RestController
public class CommentController extends AbstractController {
    @Autowired
    private CommentService comService;
    @Autowired
    private SessionManager sessManager;

    @PostMapping(value="posts/{postId}/comments")
    public CommentResponseDTO commentPost(@RequestBody CommentCreationRequestDto commentDto, @PathVariable("postId") long postId, @RequestParam("text") String text,
                               HttpSession session){
        long actorId = sessManager.authorizeLogin(session);
        return comService.addComment(commentDto, actorId);
    }

    @PutMapping(value="/comments/{id}")
    public CommentResponseDTO edit(HttpSession session, @PathVariable("id") long id, @RequestBody CommentEditRequestDTO commentReqDto){
        long actorId = sessManager.authorizeLogin(session);
        commentReqDto.setId(id);
        return comService.editComment(commentReqDto, actorId);
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
    public Set<CommentResponseDTO> getCommentsByPostId(HttpSession session, @PathVariable("id") long postId){
        long actorId = sessManager.authorizeLogin(session);
        return comService.getComments(postId);
    }
}
