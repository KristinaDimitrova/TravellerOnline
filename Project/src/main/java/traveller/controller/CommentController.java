package traveller.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.events.Comment; //delete

@RestController
public class CommentController { //todo Moni
    //create comment todo
    @PostMapping(value="user/{userId}/{postId}")
    public Comment commentPost(@PathVariable("userId") String userId, @PathVariable("postId") String postId, @RequestParam("text") String text, @RequestParam("actor_id") String id){
        //if userId exists
    }


    //edit comment todo

    //delete comment todo

    //get by id todo

    //like todo

    //unlike todo

    //view comments by post todo
}
