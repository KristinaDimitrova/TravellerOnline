package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.postDTO.PostDTO;
import traveller.model.POJOs.Post;
import traveller.model.dao.post.PostDBDao;
import traveller.model.repositories.PostRepository;
import traveller.model.repositories.UserRepository;

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

    public Post addNewPost(PostDTO postDTO, long userId){
        Post post = new Post(postDTO);
        post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));
        post.setOwner(userRepository.getById(userId));
        return postRepo.save(post);
    }

    public Post getPostById(long id) {
        Optional<Post> optionalPost = postRepo.findById(id);
        if(optionalPost.isPresent()){
            return optionalPost.get();
        }
        else throw new NotFoundException("Post not found.");
    }

    public Post editPost(long postId, PostDTO postDTO ){
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
            throw new NotFoundException("Post not found.");
        }
    }

    public void deletePost (long postId){
        postRepo.deletePost(postId);
    }

    public List<Post> getNewsFeed( long userId){
        return postDBDao.getNewsFeed(userId);
    }
}
