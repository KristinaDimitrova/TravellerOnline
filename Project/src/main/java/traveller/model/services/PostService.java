package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.postDTO.PostDTO;
import traveller.model.POJOs.Post;
import traveller.model.POJOs.User;
import traveller.model.dao.post.PostDBDao;
import traveller.model.repositories.PostRepository;
import traveller.model.repositoriesUser.UserRepository;
import traveller.service.UserService;

import java.io.NotActiveException;
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
    @Autowired
    UserRepository userRepository;

    public Post addNewPost(PostDTO postDTO, User user){
        Post post = new Post(postDTO);
        post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));
        post.setOwner(user);
        return postRepo.save(post);
    }

    public Optional<Post> getPostById(int id) {
        return postRepo.findById(id);
    }

    public Post editPost(int postId, PostDTO postDTO ){
        Optional<Post> postOptional = postRepo.findById(postId);
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            post.setLatitude(postDTO.getLatitude());
            post.setLongitude(postDTO.getLongitude());
            post.setDescription(postDTO.getDescription());
            post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));
            return postRepo.save(post);
        }
        else {
            throw new NotFoundException("There is no post with this ID!");
        }
    }

    public List<Post> getNewsFeed( long userId){
        return postDBDao.getNewsFeed(userId);
    }
}
