package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import traveller.exception.AuthorizationException;
import traveller.exception.BadRequestException;
import traveller.exception.NotFoundException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.SearchDTO;
import traveller.model.dto.fileDTO.ResponseImageDTO;
import traveller.model.dto.fileDTO.ResponseVideoDTO;
import traveller.model.dto.postDTO.RequestPostDTO;
import traveller.model.dto.postDTO.ResponsePostDTO;
import traveller.model.pojo.Image;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.model.dao.post.PostDBDao;
import traveller.model.pojo.Video;
import traveller.repository.ImageRepository;
import traveller.repository.PostRepository;
import traveller.repository.UserRepository;
import traveller.repository.VideoRepository;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
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
    @Autowired
    VideoRepository videoRepo;

    @Value("${file.path}")
    private String filePath;


    @Transactional
    public ResponsePostDTO addNewPost(RequestPostDTO postDTO, long userId){
        Post post = new Post(postDTO);
        post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));
        post.setOwner(userRepo.getById(userId));
        postRepo.save(post);
        for (long imageId : postDTO.getImageIds()){
            Image image = imageRepo.getImageById(imageId);
            image.setPost(post);
            imageRepo.save(image);
        }
        for (long videoId : postDTO.getVideoIds()){
            Video video = videoRepo.getVideoById(videoId);
            video.setPost(post);
            videoRepo.save(video);
        }

        return new ResponsePostDTO(post);
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

    public List<ResponsePostDTO> getNewsFeed(long userId, int page, int resultsPerPage) throws SQLException {
        List<Post> postList = postDBDao.getNewsFeed(userId, page, resultsPerPage);
        List<ResponsePostDTO> responsePostDTOs = new ArrayList<>();
        for(Post p :postList){
            responsePostDTOs.add(new ResponsePostDTO(p));
        }
        return responsePostDTOs;
    }

    public ResponseVideoDTO uploadVideo(MultipartFile videoFile) {
        Video video = new Video();

        File physicalFile = new File(filePath + File.separator + System.nanoTime() + ".mp4");
        try (OutputStream os = new FileOutputStream(physicalFile)) {
            os.write(videoFile.getBytes());
            video.setUrl(physicalFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoRepo.save(video);
        return new ResponseVideoDTO(video);
    }


    public ResponseImageDTO uploadImage(MultipartFile imageFile) {
        Image image  = new Image();
        File physicalFile = new File(filePath + File.separator + System.nanoTime() + ".png");
        try (OutputStream os = new FileOutputStream(physicalFile)) {
            os.write(imageFile.getBytes());
            image.setUrl(physicalFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageRepo.save(image);
        return new ResponseImageDTO(image);
    }

    public byte[] getVideoById(long videoId) {
        Video video = videoRepo.getVideoById(videoId);
        String url = video.getUrl();
        File phyFile = new File(url);
        try {
            return Files.readAllBytes(phyFile.toPath());
        } catch (IOException e) {
            throw new NotFoundException("Sorry, problem with video downloading!");
        }
    }


    public byte[] getImageById(long imageId) {
        Image image = imageRepo.getImageById(imageId);
        String url = image.getUrl();
        File phyFile = new File(url);
        try {
            return Files.readAllBytes(phyFile.toPath());
        } catch (IOException e) {
            throw new NotFoundException("Sorry, problem with image downloading!");
        }
    }
}

