package traveller.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import traveller.exceptions.AuthorizationException;
import traveller.exceptions.BadRequestException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.SearchDTO;
import traveller.model.dto.postDTO.RequestPostDTO;
import traveller.model.dto.postDTO.ResponsePostDTO;
import traveller.model.dao.post.PostDBDao;
import traveller.model.pojo.Image;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.model.repository.ImageRepository;
import traveller.model.repository.PostRepository;
import traveller.model.repository.UserRepository;

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
    @Value("${video.path")
    private String videoPath;
    @Value("${image.path")
    private String imagePath;

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
        postRepo.deletePost(postId);
        return new MessageDTO("Post deleted successfully!");
    }

    public MessageDTO likePost(int postId, long userId){
        Post post = postRepo.getPostById(postId);
        User u = userRepo.getById(userId);
        if(post.getLikers().contains(u)){
            throw  new BadRequestException("Post is already liked! ");
        }
        post.getDislikers().remove(u);
        post.getLikers().add(u);
        return new MessageDTO("Post was liked!");
    }

    public MessageDTO unlikePost(long postId, long userId){
        Post post = postRepo.getPostById(postId);
        User u = userRepo.getById(userId);
        if(!post.getLikers().contains(u)){
            throw  new BadRequestException("You need to like post before unlike it! ");
        }
        post.getLikers().remove(u);
        return new MessageDTO("Post was unliked!");
    }

    public MessageDTO dislikePost(long postId, long userId){
        Post post = postRepo.getPostById(postId);
        User u = userRepo.getById(userId);
        if(post.getDislikers().contains(u)){
            throw  new BadRequestException("Post is already disliked!");
        }
        post.getLikers().remove(u);
        post.getDislikers().add(u);
        return new MessageDTO("Post was disliked!");
    }

    public MessageDTO removeDislikeFromPost(long postId, long userId){
        Post post = postRepo.getPostById(postId);
        User u = userRepo.getById(userId);
        if(!post.getDislikers().contains(u)){
            throw new BadRequestException("You need to dislike post before remove dislike!");
        }
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

    public ResponsePostDTO uploadVideo(long postId, MultipartFile videoFile) {
        Post post = postRepo.getPostById(postId);
        if (post.getVideoUrl() != null) {
            throw new BadRequestException("You can upload only one video! ");
        }
        File physicalFile = new File(videoPath + File.separator + System.nanoTime() + ".mp4");
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
        File pFile = new File(imagePath + File.separator +System.nanoTime() +".png");
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

