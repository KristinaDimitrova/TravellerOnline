package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.SearchDTO;
import traveller.model.dto.postDTO.RequestPostDTO;
import traveller.model.dto.postDTO.ResponsePostDTO;
import traveller.model.service.PostService;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class PostController extends AbstractController {

    @Autowired
    private PostService postService;
    @Autowired
    private SessionManager sessionManager;


    @PostMapping("/posts")
    public ResponsePostDTO createPost(@RequestBody RequestPostDTO postDTO, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.addNewPost(postDTO, userId );
    }


    @PutMapping( "/posts/{id}/video")
    public ResponsePostDTO uploadVideoToPost(@PathVariable(name="id") long postId, @RequestPart MultipartFile videoFile, HttpSession session){ //all bytes
        long userId = sessionManager.authorizeLogin(session);
        return postService.uploadVideo(postId, videoFile, userId);
    }

    @PutMapping("/posts/{id}/image")
    public ResponsePostDTO uploadImageToPost(@PathVariable(name="id") long postId, @RequestPart MultipartFile imageFile, HttpSession session){ //all bytes
        long userId = sessionManager.authorizeLogin(session);
        return postService.uploadImage(postId, imageFile, userId );
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

    @PostMapping("/posts/like/{id}")
    public MessageDTO likePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.likePost(postId, userId);
    }
    @PostMapping("/posts/unlike/{id}")
    public MessageDTO unlikePost(@PathVariable (name = "id") int postId, HttpSession session){
        long userId = sessionManager.authorizeLogin(session);
        return postService.unlikePost(postId, userId);
    }

    @PostMapping("/posts/dislike/{id}")
    public MessageDTO dislikPost(@PathVariable (name = "id") int postId, HttpSession session)  {
        long userId = sessionManager.authorizeLogin(session);
        return postService.dislikePost(postId, userId);
    }

    @PostMapping("/posts/removeDislike/{id}")
    public MessageDTO removeDislikPost(@PathVariable (name = "id") int postId, HttpSession session)  {
        long userId = sessionManager.authorizeLogin(session);
        return postService.removeDislikeFromPost(postId, userId);
    }

    @PostMapping("posts/filter")
    public List<ResponsePostDTO> filter(@RequestBody SearchDTO searchDTO, HttpSession session) throws SQLException {
        sessionManager.authorizeLogin(session);
        return postService.filter(searchDTO);
    }


    @GetMapping("posts/newsfeed")
    public List<ResponsePostDTO> getNewsfeed(HttpSession session) throws SQLException {
        long id = sessionManager.authorizeLogin(session);
        return postService.getNewsFeed(id);
    }

}
