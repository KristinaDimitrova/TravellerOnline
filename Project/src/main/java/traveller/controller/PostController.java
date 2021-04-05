package traveller.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.exception.BadRequestException;
import traveller.exception.TechnicalIssuesException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.SearchDTO;
import traveller.model.dto.post.RequestPostDTO;
import traveller.model.dto.post.ResponsePostDTO;
import traveller.service.PostService;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@Log4j2
@RestController
public class PostController extends AbstractController {

    private static final int RESULTS_PER_PAGE = 10;
    @Autowired
    private PostService postService;
    @Autowired
    private SessionManager sessionManager;

    @PostMapping("/posts")
    public ResponsePostDTO createPost(@RequestBody RequestPostDTO postDTO, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.addNewPost(postDTO, userId );
    }

    @GetMapping("/posts/{id}")
    public ResponsePostDTO getById(@PathVariable(name="id") long postId, HttpSession session ){
        sessionManager.authorizeLogin(session);
        return postService.getPostById(postId);
    }

    @PutMapping("/posts/{id}")
    public ResponsePostDTO editPost(@RequestBody RequestPostDTO postDTO, @PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.editPost(postId, postDTO, userId);
    }

    @DeleteMapping("posts/{id}")
    public MessageDTO deletePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.deletePost(postId, userId);
    }

    @PostMapping("/posts/{id}/like")
    public ResponsePostDTO likePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.likePost(postId, userId);
    }

    @PostMapping("/posts/{id}/unlike")
    public ResponsePostDTO unlikePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.unlikePost(postId, userId);
    }

    @PostMapping("/posts/{id}/dislike")
    public ResponsePostDTO dislikePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.dislikePost(postId, userId);
    }

    @PostMapping("/posts/{id}/removeDislike")
    public ResponsePostDTO removeDislikeFromPost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.removeDislikeFromPost(postId, userId);
    }

    @PostMapping("posts/filter")
    public List<ResponsePostDTO> filter(@RequestBody SearchDTO searchDTO, HttpSession session){
        sessionManager.authorizeLogin(session);
        try {
            return postService.filter(searchDTO);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new TechnicalIssuesException();
        }
    }

    @GetMapping("posts/newsfeed/{page}")
    public List<ResponsePostDTO> getNewsfeed(@PathVariable(name = "page") int pageNum, HttpSession session){
        long id = sessionManager.authorizeLogin(session);
        if(pageNum < 1){
            throw new BadRequestException("Page not found.");
        }
        try {
            return postService.getNewsFeed(id, pageNum, RESULTS_PER_PAGE);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new TechnicalIssuesException();
        }
    }

}
