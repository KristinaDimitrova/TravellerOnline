package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import traveller.model.POJOs.Post;
import traveller.model.dao.post.PostDBDao;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PostController {

    @Autowired
    private PostDBDao postDBDao;

    @PostMapping("/post")
    public Post createPost(HttpServletRequest req){
        Post p = new Post();

        return p;
    }
}
