package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import traveller.exception.AuthorizationException;
import traveller.exception.BadRequestException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.SearchDTO;
import traveller.model.dto.postDTO.RequestPostDTO;
import traveller.model.dto.postDTO.ResponsePostDTO;
import traveller.model.pojo.Image;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.model.dao.post.PostDBDao;
import traveller.repository.ImageRepository;
import traveller.repository.PostRepository;
import traveller.repository.UserRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
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
    UserRepository userRepo;
    @Autowired
    ImageRepository imageRepo;

    @Value("${file.path}")
    private String filePath;


    public ResponsePostDTO addNewPost(RequestPostDTO postDTO, long userId){
        Post post = new Post(postDTO);

        post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));
        post.setOwner(userRepo.getById(userId));
        return new ResponsePostDTO(postRepo.save(post));
    }

    public ResponsePostDTO getPostById(long id) {
        return  new ResponsePostDTO(postRepo.getPostById(id));
    }

    public ResponsePostDTO editPost(long postId, RequestPostDTO postDTO, long userId) {
        Post post = postRepo.getPostById(postId);
        if (post.getOwner().getId() != userId) {
            throw new AuthorizationException("You can not edit a post that you do not own!");
        }
        post.setLatitude(postDTO.getLatitude());
        post.setLongitude(postDTO.getLongitude());
        post.setDescription(postDTO.getDescription());
        post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));
        return new ResponsePostDTO(postRepo.save(post));
    }

    public MessageDTO deletePost ( long postId, long userId){
        Post post = postRepo.getPostById(postId);
        if (post.getOwner().getId() != userId) {
            throw new AuthorizationException("You can not delete a post that you do not own!");
        }
        postRepo.delete(postRepo.getPostById(postId));
        return new MessageDTO("Post deleted successfully!");
    }

    @Transactional
    public MessageDTO likePost(int postId, long userId){
        Post post = postRepo.getPostById(postId);
        User u = userRepo.getById(userId);
        if(post.getLikers().contains(u)){
            throw  new BadRequestException("Post is already liked! ");
        }
        post.getDislikers().remove(u);
        post.getLikers().add(u);
        postRepo.save(post);
        return new MessageDTO("Post was liked!");
    }

    @Transactional
    public MessageDTO unlikePost(long postId, long userId){
        Post post = postRepo.getPostById(postId);
        User u = userRepo.getById(userId);
        if(!post.getLikers().contains(u)){
            throw  new BadRequestException("You need to like post before unlike it! ");
        }
        post.getLikers().remove(u);
        postRepo.save(post);
        return new MessageDTO("Post was unliked!");
    }

    @Transactional
    public MessageDTO dislikePost(long postId, long userId){
        Post post = postRepo.getPostById(postId);
        User u = userRepo.getById(userId);
        if(post.getDislikers().contains(u)){
            throw  new BadRequestException("Post is already disliked!");
        }
        post.getLikers().remove(u);
        post.getDislikers().add(u);
        postRepo.save(post);
        return new MessageDTO("Post was disliked!");
    }

    @Transactional
    public MessageDTO removeDislikeFromPost(long postId, long userId){
        Post post = postRepo.getPostById(postId);
        User u = userRepo.getById(userId);
        if(!post.getDislikers().contains(u)){
            throw new BadRequestException("You need to dislike post before remove dislike!");
        }
        post.getDislikers().remove(u);
        postRepo.save(post);
        return new MessageDTO("Dislike was removed from post!");
    }

    public List<ResponsePostDTO> filter(SearchDTO searchDTO) throws SQLException {
        List<ResponsePostDTO> responseList = new ArrayList<>();
        List<Post> posts = postDBDao.filter(searchDTO.getName(), searchDTO.getLocationType());
        for (Post p : posts) {
            responseList.add(new ResponsePostDTO(p));
        }
        return responseList;
    }

    public List<ResponsePostDTO> getNewsFeed( long userId) throws SQLException {
        List<Post> postList = postDBDao.getNewsFeed(userId);
        List<ResponsePostDTO> responsePostDTOs = new ArrayList<>();
        for(Post p :postList){
            responsePostDTOs.add(new ResponsePostDTO(p));
        }
        return responsePostDTOs;
    }

    public ResponsePostDTO uploadVideo(long postId, MultipartFile videoFile, long userId) {
        Post post = postRepo.getPostById(postId);
        if (post.getOwner().getId() != userId) {
            throw new AuthorizationException("You can not edit a post that you do not own!");
        }
        if (post.getVideoUrl() != null) {
            throw new BadRequestException("You can attach up only one video per post! ");
        }
        File physicalFile = new File(filePath + File.separator + System.nanoTime() + ".mp4");
        try (OutputStream os = new FileOutputStream(physicalFile)) {
            os.write(videoFile.getBytes());
            post.setVideoUrl(physicalFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Post updatedPost = postRepo.getPostById(postId);
        return new ResponsePostDTO(updatedPost);
    }


    public ResponsePostDTO uploadImage(long postId, MultipartFile imageFile) {
        Post post = postRepo.getPostById(postId);
        File pFile = new File(filePath + File.separator +System.nanoTime() +".png");
        if(post.getImages().size()==3){
            throw new BadRequestException("You can attach up to 3 photos per post!");
        }
        try( OutputStream os = new FileOutputStream(pFile);){
            os.write(imageFile.getBytes());
            Image image = new Image();
            image.setUrl(pFile.getAbsolutePath());
            image.setPost(post);
            imageRepo.save(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Post updatedPost = postRepo.getPostById(postId);
        return new ResponsePostDTO(updatedPost);
    }
}

