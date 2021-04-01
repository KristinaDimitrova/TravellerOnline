package traveller.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import traveller.model.pojo.Video;
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
    private PostDBDao postDBDao;
    @Autowired
    private LocationTypeService locationTypeService;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ImageRepository imageRepo;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private VideoRepository videoRepo;
    @Autowired
    private ModelMapper modelMapper;




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
        Post fullPost = postRepo.getPostById(post.getId());
        return convertPostToDto(fullPost);
    }

    public ResponsePostDTO getPostById(long id) {
        return convertPostToDto(postRepo.getPostById(id));
    }

    @Transactional
    public ResponsePostDTO editPost(long postId, RequestPostDTO postDTO, long userId) {
        Post post = postRepo.getPostById(postId);
        if (post.getOwner().getId() != userId) {
            throw new AuthorizationException("You can not edit a post that you do not own!");
        }
        Post post1 = convertPostToEntity(postDTO);

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


    private ResponsePostDTO convertPostToDto(Post post) {
        ResponsePostDTO postDTO = modelMapper.map(post, ResponsePostDTO.class);
        postDTO.setLocationType(post.getLocationType().getName());
        System.out.println(3);
        for(Video v : post.getVideos()){
            System.out.println( 1);
            postDTO.getVideos().add(mediaService.convertVideoEntityToImageDTO(v));
        }
        for(Image i : post.getImages()){
            System.out.println(2);
            postDTO.getImages().add(mediaService.convertImageEntityToImageDTO(i));
        }
        return postDTO;
    }

    private Post convertPostToEntity(RequestPostDTO postDto)   {
        Post post = modelMapper.map(postDto, Post.class);
        post.setCreatedAt(LocalDateTime.now());
        return post;
    }
}

