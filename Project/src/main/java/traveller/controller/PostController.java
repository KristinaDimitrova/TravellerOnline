package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveller.model.DTO.MessageDTO;
import traveller.model.DTO.SearchDTO;
import traveller.model.DTO.postDTO.RequestPostDTO;
import traveller.model.DTO.postDTO.ResponsePostDTO;
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
    public ResponsePostDTO createPost(@RequestBody RequestPostDTO postDTO, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.addNewPost(postDTO, userId );
    }


    @PutMapping( "/posts/{id}/video")
    public ResponsePostDTO uploadVideo(@PathVariable(name="id") long postId, @RequestPart MultipartFile videoFile, HttpSession session){ //all bytes
        sessionManager.authorizeLogin(session);
        return postService.uploadVideo(postId, videoFile);
    }

    @PutMapping("/post/{id}/image")
    public ResponsePostDTO uploadImage(@PathVariable(name="id") long postId, @RequestPart MultipartFile videoFile, HttpSession session){ //all bytes
        sessionManager.authorizeLogin(session);
        return postService.uploadImage(postId, videoFile);
    }

    @GetMapping("/posts/{id}")
    public ResponsePostDTO getById(@PathVariable(name="id") long postId, HttpSession session ){
        sessionManager.authorizeLogin(session);
        return postService.getPostById(postId);
    }

    @PutMapping("/post/{id}")
    public ResponsePostDTO editPost(@RequestBody RequestPostDTO postDTO, @PathVariable (name = "id") int postId, HttpSession session){
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

    @PostMapping("post/filter")
    public List<ResponsePostDTO> filter(@RequestBody SearchDTO searchDTO, HttpSession session) throws SQLException {
        sessionManager.authorizeLogin(session);
        return postService.filter(searchDTO);
    }


    @GetMapping("post/newsfeed")
    public List<ResponsePostDTO> getNewsfeed(HttpSession session) throws SQLException {
        long id = sessionManager.authorizeLogin(session);
        return postService.getNewsFeed(id);
    }

}
