package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.model.POJOs.Post;
import traveller.model.dao.post.PostDBDao;
import traveller.model.repositories.PostRepo;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private PostDBDao postDBDao;


    public Optional<Post> getPostById(int id) {
        return postRepo.findById(id);
    }

    public List<Post> getNewsFeed( long id){
        return postDBDao.getNewsFeed(id);
    }
}
