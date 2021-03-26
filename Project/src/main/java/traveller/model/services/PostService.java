package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exceptions.AuthenticationException;
import traveller.exceptions.AuthorizationException;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.MessageDTO;
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
import java.util.prefs.BackingStoreException;

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

    public Post getPostById(int id) {
        Optional<Post> optionalPost = postRepo.findById(id);
        if(optionalPost.isPresent()){
            return optionalPost.get();
        }
        else throw new NotFoundException("There is no post with this ID!");
    }

    public Post editPost(int postId, PostDTO postDTO, long userId){
        Optional<Post> postOptional = postRepo.findById(postId);
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            if(post.getOwner().getId() != userId ){
                throw new AuthorizationException("You can not edit a post that you do not own!");
            }
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

    public MessageDTO deletePost (int postId, long userId){
        Optional<Post> postOptional = postRepo.findById(postId);
        if(postOptional.isPresent()){
            Post post = postOptional.get();
            if(post.getOwner().getId() != userId ){
                throw new AuthorizationException("You can not delete a post that you do not own!");
            }
            postRepo.deletePost(postId);
            return new MessageDTO("Post deleted successfully!");
        }
        else {
            throw new NotFoundException("There is no post with this ID!");
        }
    }

    public List<Post> getNewsFeed( long userId){
        return postDBDao.getNewsFeed(userId);
    }
}
