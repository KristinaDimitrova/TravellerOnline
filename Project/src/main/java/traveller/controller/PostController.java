package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.exceptions.BadRequestException;
import traveller.model.DTO.MessageDTO;
import traveller.model.DTO.postDTO.PostDTO;
import traveller.model.POJOs.Post;
import traveller.model.services.PostService;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class PostController extends AbstractController {

    @Autowired
    private PostService postService;
    @Autowired
    private SessionManager sessionManager;


    @PostMapping("/post")
    public Post createPost( @RequestBody PostDTO postDTO, HttpSession session){
        sessionManager.authorizeLogin(session);
        return postService.addNewPost(postDTO, sessionManager.authorizeLogin(session) );
    }

    @GetMapping("/posts/{id}")
    public Post getById(@PathVariable(name="id") long postId, HttpSession session ){
        sessionManager.authorizeLogin(session);
        return postService.getPostById(postId);
    }

    @PutMapping("/post/{id}")
    public Post editPost(@RequestBody PostDTO postDTO, @PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.editPost(postId, postDTO, userId);
    }

    @DeleteMapping("post/{id}")
    public MessageDTO deletePost(@PathVariable (name = "id") int postId, HttpSession session) {
        long userId = sessionManager.authorizeLogin(session);
        return postService.deletePost(postId, userId);
    }

    @PostMapping("/post/like/{id}")
    public MessageDTO likeOrUnlikePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.likeOrUnlikePost(postId, userId);
    }

  


    @PostMapping("/post/dislike/{id}")
    public MessageDTO dislikeOrUndislikePost(@PathVariable (name = "id") int postId, HttpSession session)  {
        long userId = sessionManager.authorizeLogin(session);
        return postService.dislikeOrUndislikePost(postId, userId);
    }

    @GetMapping("post/newsfeed")
    public List<Post> getNewsfeed(HttpSession session) {
        long id = sessionManager.authorizeLogin(session);
        return postService.getNewsFeed(id);
    }


    @GetMapping("post/search")
    public List<Post> search(){
        return null;
    }

}
