package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.model.DTO.postDTO.PostDTO;
import traveller.model.POJOs.Post;
import traveller.model.dao.post.PostDBDao;
import traveller.model.repositories.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private PostDBDao postDBDao;
    @Autowired
    LocationTypeService locationTypeService;

    public Post addNewPost(PostDTO postDTO, long ownerId){
        Post post = new Post(postDTO);
        post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));

        return postRepo.save(post);
    }

    public Optional<Post> getPostById(int id) {
        return postRepo.findById(id);
    }

    public List<Post> getNewsFeed( long userId){
        return postDBDao.getNewsFeed(userId);
    }
}
