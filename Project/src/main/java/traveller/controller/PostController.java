package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.DTO.MessageDTO;
import traveller.model.DTO.SearchDTO;
import traveller.model.DTO.postDTO.RequestPostDTO;
import traveller.model.POJOs.Post;
import traveller.model.services.PostService;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
public class PostController extends AbstractController {

    @Autowired
    private PostService postService;
    @Autowired
    private SessionManager sessionManager;


    @PostMapping("/post")
    public Post createPost(@RequestBody RequestPostDTO postDTO, HttpSession session){
        sessionManager.authorizeLogin(session);
        return postService.addNewPost(postDTO, sessionManager.authorizeLogin(session) );
    }

    @GetMapping("/posts/{id}")
    public Post getById(@PathVariable(name="id") long postId, HttpSession session ){
        sessionManager.authorizeLogin(session);
        return postService.getPostById(postId);
    }

    @PutMapping("/post/{id}")
    public Post editPost(@RequestBody RequestPostDTO postDTO, @PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.editPost(postId, postDTO, userId);
    }

    @DeleteMapping("post/{id}")
    public MessageDTO deletePost(@PathVariable (name = "id") int postId, HttpSession session) {
        long userId = sessionManager.authorizeLogin(session);
        return postService.deletePost(postId, userId);
    }

    @PostMapping("/post/like/{id}")
    public MessageDTO likePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.likePost(postId, userId);
    }
    @PostMapping("/post/unlike/{id}")
    public MessageDTO unlikePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.unlikePost(postId, userId);
    }

    @PostMapping("/post/dislike/{id}")
    public MessageDTO dislikPost(@PathVariable (name = "id") int postId, HttpSession session)  {
        long userId = sessionManager.authorizeLogin(session);
        return postService.dislikePost(postId, userId);
    }

    @PostMapping("/post/removeDislike/{id}")
    public MessageDTO removeDislikPost(@PathVariable (name = "id") int postId, HttpSession session)  {
        long userId = sessionManager.authorizeLogin(session);
        return postService.removeDislikeFromPost(postId, userId);
    }

    @PostMapping("post/search")
    public List<Post> search(@RequestBody SearchDTO searchDTO, HttpSession session){
        sessionManager.authorizeLogin(session);
        return null;
    }


    @GetMapping("post/newsfeed")
    public List<Post> getNewsfeed(HttpSession session) {
        long id = sessionManager.authorizeLogin(session);
        return postService.getNewsFeed(id);
    }

}
