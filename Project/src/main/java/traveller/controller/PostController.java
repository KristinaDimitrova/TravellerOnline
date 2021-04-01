package traveller.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.exception.BadRequestException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.SearchDTO;
import traveller.model.dto.postDTO.RequestPostDTO;
import traveller.model.dto.postDTO.ResponsePostDTO;
import traveller.service.PostService;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;


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
    public MessageDTO deletePost(@PathVariable (name = "id") int postId, HttpSession session) {
        long userId = sessionManager.authorizeLogin(session);
        return postService.deletePost(postId, userId);
    }

    @PostMapping("/posts/{id}/like")
    public MessageDTO likePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.likePost(postId, userId);
    }

    @PostMapping("/posts/{id}/unlike")
    public MessageDTO unlikePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.unlikePost(postId, userId);
    }

    @PostMapping("/posts/{id}/dislike")
    public MessageDTO dislikePost(@PathVariable (name = "id") int postId, HttpSession session)  {
        long userId = sessionManager.authorizeLogin(session);
        return postService.dislikePost(postId, userId);
    }

    @PostMapping("/posts/{id}/removeDislike")
    public MessageDTO removeDislikeFromPost(@PathVariable (name = "id") int postId, HttpSession session)  {
        long userId = sessionManager.authorizeLogin(session);
        return postService.removeDislikeFromPost(postId, userId);
    }

    @PostMapping("posts/filter")
    public List<ResponsePostDTO> filter(@RequestBody SearchDTO searchDTO, HttpSession session) throws SQLException {
        sessionManager.authorizeLogin(session);
        return postService.filter(searchDTO);
    }

    @GetMapping("posts/newsfeed/{page}")
    public List<ResponsePostDTO> getNewsfeed(@PathVariable(name = "page") int pageNum, HttpSession session) throws SQLException {
        long id = sessionManager.authorizeLogin(session);
        if(pageNum < 1){
            throw new BadRequestException("Page not found.");
        }
        return postService.getNewsFeed(id, pageNum, RESULTS_PER_PAGE);
    }

}
