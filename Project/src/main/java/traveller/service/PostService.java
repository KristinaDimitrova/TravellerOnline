package traveller.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exception.AuthorizationException;
import traveller.exception.BadRequestException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.SearchDTO;
import traveller.model.dto.post.RequestPostDTO;
import traveller.model.dto.post.ResponsePostDTO;
import traveller.model.pojo.*;
import traveller.model.dao.post.PostDatabaseDAO;
import traveller.repository.ImageRepository;
import traveller.repository.PostRepository;
import traveller.repository.UserRepository;
import traveller.repository.VideoRepository;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepo;
    @Autowired
    private PostDatabaseDAO postDBDao;
    @Autowired
    private LocationTypeService locationTypeService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ImageRepository imageRepo;
    @Autowired
    private VideoRepository videoRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    CommentService commentService;

    @Transactional
    public ResponsePostDTO  addNewPost(RequestPostDTO postDTO, long userId){
        Post post = convertPostToEntity(postDTO);
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
        return convertPostToDto(postRepo.getPostById(post.getId()));
    }

    public ResponsePostDTO getPostById(long id) {
        Post post = postRepo.getPostById(id);
        System.out.println(post.getVideos().size());
        System.out.println(post.getImages().size());
        return convertPostToDto(post);
    }

    @Transactional
    public ResponsePostDTO editPost(long postId, RequestPostDTO postDTO, long userId) {
        Post post = postRepo.getPostById(postId);
        if (post.getOwner().getId() != userId) {
            throw new AuthorizationException("You can not edit a post that you do not own!");
        }
        post = convertPostToEntity(postDTO);
        post.setLocationType(locationTypeService.getByName(postDTO.getLocationType()));
        return convertPostToDto(post);
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
            responseList.add(convertPostToDto(p));
        }
        return responseList;
    }

    public List<ResponsePostDTO> getNewsFeed(long userId, int page, int resultsPerPage) throws SQLException {
        List<Post> postList = postDBDao.getNewsFeed(userId, page, resultsPerPage);
        List<ResponsePostDTO> responsePostDTOs = new ArrayList<>();
        for(Post p :postList){
            responsePostDTOs.add(convertPostToDto(p));
        }
        return responsePostDTOs;
    }


    public ResponsePostDTO convertPostToDto(Post post) {
        return modelMapper.map(post, ResponsePostDTO.class);
    }

    public Post convertPostToEntity(RequestPostDTO postDto)   {
        Post post = modelMapper.map(postDto, Post.class);
        post.setCreatedAt(LocalDateTime.now());
        return post;
    }
}

