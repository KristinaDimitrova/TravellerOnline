package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import traveller.model.dao.post.PostDBDao;

@RestController
public class PostController extends MotherController{

    @Autowired
    private PostDBDao postDBDao;
}
