package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.exceptions.BadRequestException;
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

/*
    @PostMapping("/post")
    public Post createPost( @RequestBody PostDTO postDTO, HttpSession session){
        if(sessionManager.isUserLoggedIn(session)){
            return postService.addNewPost(postDTO, sessionManager.isUserLoggedIn(session) );
        }
        else{
            throw new traveller.exceptions.AuthenticationException("You need to be logged in!");
        }
    }
*/

    @GetMapping("/posts/{id}")
    public Post getById(@PathVariable int id, HttpSession session ){
        if(sessionManager.isUserLoggedIn(session)){
            return postService.getPostById(id);
        }
        else{
            throw new traveller.exceptions.AuthenticationException("You need to be logged in!");
        }
    }


    @PutMapping("/posts/{id}")
    public Post editPost(){
        // edit post
        return new Post();
    }


    @DeleteMapping("posts/{id}")
    public String deletePost(){
        //delete post (comments delete cascade)
        return "Post was deleted successfully!";
    }

    @GetMapping("/posts/like/{id}")
    public String likeOrUnlikePost(@PathVariable int id ) throws BadRequestException{
        //if post does not exist throws BAD_REQUEST Exception
        // -if post is NOT liked and is NOT disliked : -> insert
        // user id(get from session), and post id (from path ) into users_likes_posts(ulp)
        // - if post is NOT liked and IS disliked -> delete from users_dislikes_posts and insert into ulp
        // - if  post IS liked -> delete from ulp
        return ":D";
    }

    @GetMapping("/posts/dislike/{id}")
    public String dislikeOrUndislikePost(@PathVariable int id ) throws BadRequestException {
        //if post does not exist throws BAD_REQUEST Exception
        // -if post is NOT liked and is NOT disliked : -> insert
        // user id(get from session), and post id (from path ) into users_dislikes_posts(udp)
        // - if post is NOT liked and IS disliked -> delete from users_dislikes_post s
        // - if  post IS liked delete from ulp -> delete from users_likes_posts and insert into udp
        return ":D";
    }

    @GetMapping("posts/newsfeed")
    public List<Post> getNewsfeed(HttpSession session) throws SQLException {
        long id = (long) session.getAttribute(SessionManager.LOGGED_IN);
        return postService.getNewsFeed(id);
    }

    @GetMapping("posts/search")
    public List<Post> search() throws SQLException {
        return null;
    }

}
