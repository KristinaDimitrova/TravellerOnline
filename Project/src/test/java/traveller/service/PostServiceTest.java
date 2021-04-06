package traveller.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import traveller.exception.AuthorizationException;
import traveller.exception.BadRequestException;
import traveller.model.dao.post.PostDatabaseDAO;
import traveller.model.dto.SearchDTO;
import traveller.model.dto.locationType.LocationTypeDTO;
import traveller.model.dto.post.RequestPostDTO;
import traveller.model.dto.post.ResponsePostDTO;
import traveller.model.dto.user.OwnerDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Image;
import traveller.model.pojo.LocationType;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.model.pojo.Video;
import traveller.registration.Role;
import traveller.repository.ImageRepository;
import traveller.repository.PostRepository;
import traveller.repository.UserRepository;
import traveller.repository.VideoRepository;

@ContextConfiguration(classes = {PostService.class, Post.class, ModelMapper.class, CommentService.class,
        PostDatabaseDAO.class, LocationTypeService.class, SearchDTO.class})
@ExtendWith(SpringExtension.class)
public class PostServiceTest {
    @Autowired
    private SearchDTO searchDTO;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ImageRepository imageRepository;

    @MockBean
    private LocationTypeService locationTypeService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private Post post;

    @MockBean
    private PostDatabaseDAO postDatabaseDAO;

    @MockBean
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private VideoRepository videoRepository;

    @Test
    public void testGetPostById() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER);
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setId(123L);
        user.setDeleted(true);
        user.setAge(1);
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setPosts(new ArrayList<Post>());
        user.setDislikedPosts(new HashSet<Post>());
        user.setFollowedUsers(new ArrayList<User>());
        user.setUsername("janedoe");
        user.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        user.setLikedPosts(new HashSet<Post>());
        user.setLikedComment(new HashSet<Comment>());
        user.setComments(new HashSet<Comment>());
        user.setFollowers(new HashSet<User>());

        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());

        Post post = new Post();
        post.setOwner(user);
        post.setComments(new ArrayList<Comment>());
        post.setImages(new ArrayList<Image>());
        post.setLocationType(locationType);
        post.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        post.setId(123L);
        post.setLikers(new HashSet<User>());
        post.setLongitude("Longitude");
        post.setVideos(new ArrayList<Video>());
        post.setLatitude("Latitude");
        post.setDescription("The characteristics of someone or something");
        post.setDislikers(new HashSet<User>());
        when(this.postRepository.getPostById(anyLong())).thenReturn(post);
        ResponsePostDTO actualPostById = this.postService.getPostById(123L);
        assertEquals("Longitude", actualPostById.getLongitude());
        assertEquals("The characteristics of someone or something", actualPostById.getDescription());
        assertEquals(123L, actualPostById.getId());
        assertEquals(0, actualPostById.getDislikes());
        assertEquals("Latitude", actualPostById.getLatitude());
        assertEquals(0, actualPostById.getLikes());
        LocationTypeDTO locationType1 = actualPostById.getLocationType();
        assertEquals(123L, locationType1.getId());
        assertEquals("Name", locationType1.getName());
        OwnerDTO owner = actualPostById.getOwner();
        assertEquals("Jane", owner.getFirstName());
        assertEquals("Doe", owner.getLastName());
        assertEquals("janedoe", owner.getUsername());
        assertEquals(123L, owner.getId());
        verify(this.postRepository).getPostById(anyLong());
    }

    @Test
    public void testDeletePost() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER);
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setId(123L);
        user.setDeleted(true);
        user.setAge(1);
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setPosts(new ArrayList<Post>());
        user.setDislikedPosts(new HashSet<Post>());
        user.setFollowedUsers(new ArrayList<User>());
        user.setUsername("janedoe");
        user.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        user.setLikedPosts(new HashSet<Post>());
        user.setLikedComment(new HashSet<Comment>());
        user.setComments(new HashSet<Comment>());
        user.setFollowers(new HashSet<User>());

        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());

        Post post = new Post();
        post.setOwner(user);
        post.setComments(new ArrayList<Comment>());
        post.setImages(new ArrayList<Image>());
        post.setLocationType(locationType);
        post.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        post.setId(123L);
        post.setLikers(new HashSet<User>());
        post.setLongitude("Longitude");
        post.setVideos(new ArrayList<Video>());
        post.setLatitude("Latitude");
        post.setDescription("The characteristics of someone or something");
        post.setDislikers(new HashSet<User>());
        doNothing().when(this.postRepository).delete((Post) any());
        when(this.postRepository.getPostById(anyLong())).thenReturn(post);
        assertEquals("Post deleted successfully!", this.postService.deletePost(123L, 123L).getText());
        verify(this.postRepository).delete((Post) any());
        verify(this.postRepository, times(2)).getPostById(anyLong());
    }

    @Test
    public void testDeletePost2() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER);
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setId(0L);
        user.setDeleted(true);
        user.setAge(1);
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setPosts(new ArrayList<Post>());
        user.setDislikedPosts(new HashSet<Post>());
        user.setFollowedUsers(new ArrayList<User>());
        user.setUsername("janedoe");
        user.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        user.setLikedPosts(new HashSet<Post>());
        user.setLikedComment(new HashSet<Comment>());
        user.setComments(new HashSet<Comment>());
        user.setFollowers(new HashSet<User>());

        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());

        Post post = new Post();
        post.setOwner(user);
        post.setComments(new ArrayList<Comment>());
        post.setImages(new ArrayList<Image>());
        post.setLocationType(locationType);
        post.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        post.setId(123L);
        post.setLikers(new HashSet<User>());
        post.setLongitude("Longitude");
        post.setVideos(new ArrayList<Video>());
        post.setLatitude("Latitude");
        post.setDescription("The characteristics of someone or something");
        post.setDislikers(new HashSet<User>());
        doNothing().when(this.postRepository).delete((Post) any());
        when(this.postRepository.getPostById(anyLong())).thenReturn(post);
        assertThrows(AuthorizationException.class, () -> this.postService.deletePost(123L, 123L));
        verify(this.postRepository).getPostById(anyLong());
    }

    @Test //KRISI must push changes todo
    public void testFilter() throws SQLException {
        when(this.postDatabaseDAO.filter(anyString(), anyString())).thenReturn(new ArrayList<Post>());
        assertTrue(this.postService.filter(this.searchDTO).isEmpty());
        verify(this.postDatabaseDAO).filter(anyString(), anyString());
    }

    @Test
    public void testFilter2() throws SQLException {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER);
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setId(123L);
        user.setDeleted(true);
        user.setAge(0);
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setPosts(new ArrayList<Post>());
        user.setDislikedPosts(new HashSet<Post>());
        user.setFollowedUsers(new ArrayList<User>());
        user.setUsername("janedoe");
        user.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        user.setLikedPosts(new HashSet<Post>());
        user.setLikedComment(new HashSet<Comment>());
        user.setComments(new HashSet<Comment>());
        user.setFollowers(new HashSet<User>());

        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());

        Post post = new Post();
        post.setOwner(user);
        post.setComments(new ArrayList<Comment>());
        post.setImages(new ArrayList<Image>());
        post.setLocationType(locationType);
        post.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        post.setId(123L);
        post.setLikers(new HashSet<User>());
        post.setLongitude("Longitude");
        post.setVideos(new ArrayList<Video>());
        post.setLatitude("Latitude");
        post.setDescription("The characteristics of someone or something");
        post.setDislikers(new HashSet<User>());

        ArrayList<Post> postList = new ArrayList<Post>();
        postList.add(post);
        when(this.postDatabaseDAO.filter(anyString(), anyString())).thenReturn(postList);
        assertEquals(1, this.postService.filter(this.searchDTO).size());
        verify(this.postDatabaseDAO).filter(anyString(), anyString());
    }

    @Test
    public void testGetNewsFeed() throws SQLException {
        when(this.postDatabaseDAO.getNewsFeed(anyLong(), anyInt(), anyInt())).thenReturn(new ArrayList<Post>());
        assertTrue(this.postService.getNewsFeed(123L, 1, 1).isEmpty());
        verify(this.postDatabaseDAO).getNewsFeed(anyLong(), anyInt(), anyInt());
    }

    @Test
    public void testGetNewsFeed2() throws SQLException {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER);
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setId(123L);
        user.setDeleted(true);
        user.setAge(0);
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setPosts(new ArrayList<Post>());
        user.setDislikedPosts(new HashSet<Post>());
        user.setFollowedUsers(new ArrayList<User>());
        user.setUsername("janedoe");
        user.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        user.setLikedPosts(new HashSet<Post>());
        user.setLikedComment(new HashSet<Comment>());
        user.setComments(new HashSet<Comment>());
        user.setFollowers(new HashSet<User>());

        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());

        Post post = new Post();
        post.setOwner(user);
        post.setComments(new ArrayList<Comment>());
        post.setImages(new ArrayList<Image>());
        post.setLocationType(locationType);
        post.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        post.setId(123L);
        post.setLikers(new HashSet<User>());
        post.setLongitude("Longitude");
        post.setVideos(new ArrayList<Video>());
        post.setLatitude("Latitude");
        post.setDescription("The characteristics of someone or something");
        post.setDislikers(new HashSet<User>());

        ArrayList<Post> postList = new ArrayList<Post>();
        postList.add(post);
        when(this.postDatabaseDAO.getNewsFeed(anyLong(), anyInt(), anyInt())).thenReturn(postList);
        assertEquals(1, this.postService.getNewsFeed(123L, 1, 1).size());
        verify(this.postDatabaseDAO).getNewsFeed(anyLong(), anyInt(), anyInt());
    }

    @Test
    public void testConvertPostToDto() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenThrow(new BadRequestException("foo"));
        assertThrows(BadRequestException.class, () -> this.postService.convertPostToDto(this.post));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertPostToDto2() {
        ResponsePostDTO responsePostDTO = new ResponsePostDTO();
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(responsePostDTO);
        ResponsePostDTO actualConvertPostToDtoResult = this.postService.convertPostToDto(this.post);
        assertSame(responsePostDTO, actualConvertPostToDtoResult);
        assertEquals(0, actualConvertPostToDtoResult.getLikes());
        assertEquals(0, actualConvertPostToDtoResult.getDislikes());
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertPostToDto3() {
        ResponsePostDTO responsePostDTO = mock(ResponsePostDTO.class);
        doNothing().when(responsePostDTO).setDislikes(anyInt());
        doNothing().when(responsePostDTO).setLikes(anyInt());
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(responsePostDTO);
        this.postService.convertPostToDto(this.post);
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
        verify(responsePostDTO).setDislikes(anyInt());
        verify(responsePostDTO).setLikes(anyInt());
    }

    @Test
    public void testConvertPostToEntity() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenThrow(new BadRequestException("foo"));
        assertThrows(BadRequestException.class, () -> this.postService.convertPostToEntity(new RequestPostDTO()));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertPostToEntity2() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER);
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setId(123L);
        user.setDeleted(true);
        user.setAge(0);
        user.setEnabled(true);
        user.setFirstName("Jane");
        user.setPosts(new ArrayList<Post>());
        user.setDislikedPosts(new HashSet<Post>());
        user.setFollowedUsers(new ArrayList<User>());
        user.setUsername("janedoe");
        user.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        user.setLikedPosts(new HashSet<Post>());
        user.setLikedComment(new HashSet<Comment>());
        user.setComments(new HashSet<Comment>());
        user.setFollowers(new HashSet<User>());

        LocationType locationType = new LocationType();
        locationType.setId(123L);
        locationType.setName("Name");
        locationType.setPosts(new ArrayList<Post>());

        Post post = new Post();
        post.setOwner(user);
        post.setComments(new ArrayList<Comment>());
        post.setImages(new ArrayList<Image>());
        post.setLocationType(locationType);
        post.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        post.setId(123L);
        post.setLikers(new HashSet<User>());
        post.setLongitude("Longitude");
        post.setVideos(new ArrayList<Video>());
        post.setLatitude("Latitude");
        post.setDescription("The characteristics of someone or something");
        post.setDislikers(new HashSet<User>());
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(post);
        assertSame(post, this.postService.convertPostToEntity(new RequestPostDTO()));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertPostToEntity3() {
        Post post = mock(Post.class);
        doNothing().when(post).setCreatedAt((java.time.LocalDateTime) any());
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(post);
        this.postService.convertPostToEntity(new RequestPostDTO());
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
        verify(post).setCreatedAt((java.time.LocalDateTime) any());
    }
}

