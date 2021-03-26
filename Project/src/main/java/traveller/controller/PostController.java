package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.exceptions.BadRequestException;
import traveller.model.POJOs.Post;
import traveller.model.dao.post.PostDBDao;
import traveller.model.services.PostService;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@RestController
public class PostController extends AbstractController {

    @Autowired
    private PostService postService;

    @PostMapping("/post")
    public Post createPost(@RequestBody Post post){
        //insert post into DB
        return post;
    }

    @PutMapping("/post/{id}")
    public Post editPost(){
        // edit post
        return new Post();
    }

    @GetMapping("/post/{id}")
    public Post getById(@PathVariable int id ){


        return new Post();
    }

    @DeleteMapping("post/{id}")
    public String deletePost(){
        //delete post (comments delete cascade)
        return "Post was deleted successfully!";
    }

    @GetMapping("/post/like-or-unlike/{id}")
    public String likeOrUnlikePost(@PathVariable int id ) throws BadRequestException{
        //if post does not exist throws BAD_REQUEST Exception
        // -if post is NOT liked and is NOT disliked : -> insert
        // user id(get from session), and post id (from path ) into users_likes_posts(ulp)
        // - if post is NOT liked and IS disliked -> delete from users_dislikes_posts and insert into ulp
        // - if  post IS liked -> delete from ulp
        return ":D";
    }

    @GetMapping("/post/like-or-unlike/{id}")
    public String dislikeOrUndislikePost(@PathVariable int id ) throws BadRequestException {
        //if post does not exist throws BAD_REQUEST Exception
        // -if post is NOT liked and is NOT disliked : -> insert
        // user id(get from session), and post id (from path ) into users_dislikes_posts(udp)
        // - if post is NOT liked and IS disliked -> delete from users_dislikes_post s
        // - if  post IS liked delete from ulp -> delete from users_likes_posts and insert into udp
        return ":D";
    }

    @GetMapping("post/newsfeed")
    public List<Post> getNewsfeed(HttpSession session) throws SQLException {
        long id = (long) session.getAttribute(SessionManager.LOGGED_IN);
        return postService.getNewsFeed(id);
    }

    @GetMapping("post/search")
    public List<Post> search() throws SQLException {
        return null;
    }


}
