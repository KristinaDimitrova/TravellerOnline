package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exceptions.AuthorizationException;
import traveller.exceptions.BadRequestException;
import traveller.model.DTO.MessageDTO;
import traveller.model.DTO.SearchDTO;
import traveller.model.DTO.postDTO.RequestPostDTO;
import traveller.model.DTO.postDTO.ResponsePostDTO;
import traveller.model.POJOs.Post;
import traveller.model.POJOs.User;
import traveller.model.dao.post.PostDBDao;
import traveller.model.repositories.PostRepository;
import traveller.model.repositories.UserRepository;
import java.util.List;

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

    public Post addNewPost(RequestPostDTO postDTO, long userId){
        Post post = new Post(postDTO);
        post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));
        post.setOwner(userRepository.getById(userId));
        return postRepo.save(post);
    }

    public Post getPostById(long id) {
        return postRepo.getPostById(id);
    }

    public Post editPost(long postId, RequestPostDTO postDTO, long userId) {
        Post post = getPostById(postId);
        if (post.getOwner().getId() != userId) {
            throw new AuthorizationException("You can not edit a post that you do not own!");
        }
        post.setLatitude(postDTO.getLatitude());
        post.setLongitude(postDTO.getLongitude());
        post.setDescription(postDTO.getDescription());
        post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));
        return postRepo.save(post);
    }

    public MessageDTO deletePost ( long postId, long userId){
        Post post = getPostById(postId);
        if (post.getOwner().getId() != userId) {
            throw new AuthorizationException("You can not delete a post that you do not own!");
        }
        postRepo.deletePost(postId);
        return new MessageDTO("Post deleted successfully!");
    }


    public MessageDTO likePost(int postId, long userId){
        Post post = getPostById(postId);
        User u = userRepository.getById(userId);
        if(post.getLikers().contains(u)){
            throw  new BadRequestException("Post is already liked! ");
        }
        post.getDislikers().remove(u);
        post.getLikers().add(u);
        return new MessageDTO("Post was liked!");
    }

    public MessageDTO unlikePost(long postId, long userId){
        Post post = getPostById(postId);
        User u = userRepository.getById(userId);
        if(!post.getLikers().contains(u)){
            throw  new BadRequestException("You need to like post before unlike it! ");
        }
        post.getLikers().remove(u);
        return new MessageDTO("Post was unliked!");
    }

    public MessageDTO dislikePost(long postId, long userId){
        Post post = getPostById(postId);
        User u = userRepository.getById(userId);
        if(post.getDislikers().contains(u)){
            throw  new BadRequestException("Post is already disliked!");
        }
        post.getLikers().remove(u);
        post.getDislikers().add(u);
        return new MessageDTO("Post was disliked!");
    }

    public MessageDTO removeDislikeFromPost(long postId, long userId){
        Post post = getPostById(postId);
        User u = userRepository.getById(userId);
        if(!post.getDislikers().contains(u)){
            throw new BadRequestException("You need to dislike post before remove dislike!");
        }
        return new MessageDTO("Dislike was removed from post!");
    }

    public List<ResponsePostDTO> filter(SearchDTO searchDTO){

    }

    public List<Post> getNewsFeed( long userId){
        return postDBDao.getNewsFeed(userId);
    }
}

