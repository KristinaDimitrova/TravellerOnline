package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exceptions.AuthorizationException;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.MessageDTO;
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

    public Post editPost(long postId, PostDTO postDTO, long userId){
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
            throw new NotFoundException("Post not found.");
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

    public MessageDTO likeOrUnlikePost(int postId, long userId){
        String message = "";
        getPostById(postId);

        // -if post is NOT liked and is NOT disliked : -> insert
        // user id(get from session), and post id (from path ) into users_likes_posts(ulp)
        // - if post is NOT liked and IS disliked -> delete from users_dislikes_posts and insert into ulp
        // - if  post IS liked -> delete from ulp

        return new MessageDTO(message);
    }

    public MessageDTO dislikeOrUndislikePost(long postId, long userId){
        String message = "";
        //if post does not exist throws BAD_REQUEST Exception
        // -if post is NOT liked and is NOT disliked : -> insert
        // user id(get from session), and post id (from path ) into users_dislikes_posts(udp)
        // - if post is NOT liked and IS disliked -> delete from users_dislikes_post s
        // - if  post IS liked delete from ulp -> delete from users_likes_posts and insert into udp
        return new MessageDTO(message);
    }

    public List<Post> getNewsFeed( long userId){
        return postDBDao.getNewsFeed(userId);
    }
}
