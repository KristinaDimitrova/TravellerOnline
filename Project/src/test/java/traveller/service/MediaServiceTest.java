package traveller.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.StringInputStream;

import java.io.ByteArrayInputStream;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.http.client.methods.HttpDelete;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import traveller.exception.TechnicalIssuesException;
import traveller.model.dto.file.ImageDTO;
import traveller.model.dto.file.VideoDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Image;
import traveller.model.pojo.LocationType;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.model.pojo.Video;
import traveller.registration.Role;
import traveller.repository.ImageRepository;
import traveller.repository.VideoRepository;

@ContextConfiguration(classes = {MediaService.class, Image.class, Video.class, ModelMapper.class})
@ExtendWith(SpringExtension.class)
public class MediaServiceTest {
    @Autowired
    private Video video;

    @MockBean
    private AmazonS3 amazonS3;

    @Autowired
    private Image image;

    @MockBean
    private ImageRepository imageRepository;

    @Autowired
    private MediaService mediaService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private VideoRepository videoRepository;

    @Test
    public void testGetVideoById() throws SdkClientException, UnsupportedEncodingException {
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

        Video video = new Video();
        video.setFileName("foo.txt");
        video.setPost(post);
        video.setId(123L);
        when(this.videoRepository.getVideoById(anyLong())).thenReturn(video);

        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new StringInputStream("Lorem ipsum dolor sit amet."));
        when(this.amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);
        byte[] actualVideoById = this.mediaService.getVideoById(123L);
        assertEquals(27, actualVideoById.length);
        assertEquals('L', actualVideoById[0]);
        assertEquals('o', actualVideoById[1]);
        assertEquals('r', actualVideoById[2]);
        assertEquals('e', actualVideoById[3]);
        assertEquals('m', actualVideoById[4]);
        assertEquals(' ', actualVideoById[5]);
        assertEquals(' ', actualVideoById[21]);
        assertEquals('a', actualVideoById[22]);
        assertEquals('m', actualVideoById[23]);
        assertEquals('e', actualVideoById[24]);
        assertEquals('t', actualVideoById[25]);
        assertEquals('.', actualVideoById[26]);
        verify(this.amazonS3).getObject(anyString(), anyString());
        verify(this.videoRepository).getVideoById(anyLong());
    }

    @Test
    public void testGetVideoById2() throws SdkClientException {
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

        Video video = new Video();
        video.setFileName("foo.txt");
        video.setPost(post);
        video.setId(123L);
        when(this.videoRepository.getVideoById(anyLong())).thenReturn(video);
        S3Object s3Object = mock(S3Object.class);
        ByteArrayInputStream in = new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes());
        when(s3Object.getObjectContent()).thenReturn(new S3ObjectInputStream(in, new HttpDelete()));
        when(this.amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);
        byte[] actualVideoById = this.mediaService.getVideoById(123L);
        assertEquals(24, actualVideoById.length);
        assertEquals('A', actualVideoById[0]);
        assertEquals('A', actualVideoById[1]);
        assertEquals('A', actualVideoById[2]);
        assertEquals('A', actualVideoById[3]);
        assertEquals('A', actualVideoById[4]);
        assertEquals('A', actualVideoById[5]);
        assertEquals('A', actualVideoById[18]);
        assertEquals('A', actualVideoById[19]);
        assertEquals('A', actualVideoById[20]);
        assertEquals('A', actualVideoById[21]);
        assertEquals('A', actualVideoById[22]);
        assertEquals('A', actualVideoById[23]);
        verify(this.amazonS3).getObject(anyString(), anyString());
        verify(s3Object).getObjectContent();
        verify(this.videoRepository).getVideoById(anyLong());
    }

    @Test
    public void testGetImageById() throws SdkClientException, UnsupportedEncodingException {
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

        Image image = new Image();
        image.setFileName("foo.txt");
        image.setPost(post);
        image.setId(123L);
        when(this.imageRepository.getImageById(anyLong())).thenReturn(image);

        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new StringInputStream("Lorem ipsum dolor sit amet."));
        when(this.amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);
        byte[] actualImageById = this.mediaService.getImageById(123L);
        assertEquals(27, actualImageById.length);
        assertEquals('L', actualImageById[0]);
        assertEquals('o', actualImageById[1]);
        assertEquals('r', actualImageById[2]);
        assertEquals('e', actualImageById[3]);
        assertEquals('m', actualImageById[4]);
        assertEquals(' ', actualImageById[5]);
        assertEquals(' ', actualImageById[21]);
        assertEquals('a', actualImageById[22]);
        assertEquals('m', actualImageById[23]);
        assertEquals('e', actualImageById[24]);
        assertEquals('t', actualImageById[25]);
        assertEquals('.', actualImageById[26]);
        verify(this.amazonS3).getObject(anyString(), anyString());
        verify(this.imageRepository).getImageById(anyLong());
    }

    @Test
    public void testGetImageById2() throws SdkClientException {
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

        Image image = new Image();
        image.setFileName("foo.txt");
        image.setPost(post);
        image.setId(123L);
        when(this.imageRepository.getImageById(anyLong())).thenReturn(image);
        S3Object s3Object = mock(S3Object.class);
        ByteArrayInputStream in = new ByteArrayInputStream("AAAAAAAAAAAAAAAAAAAAAAAA".getBytes());
        when(s3Object.getObjectContent()).thenReturn(new S3ObjectInputStream(in, new HttpDelete()));
        when(this.amazonS3.getObject(anyString(), anyString())).thenReturn(s3Object);
        byte[] actualImageById = this.mediaService.getImageById(123L);
        assertEquals(24, actualImageById.length);
        assertEquals('A', actualImageById[0]);
        assertEquals('A', actualImageById[1]);
        assertEquals('A', actualImageById[2]);
        assertEquals('A', actualImageById[3]);
        assertEquals('A', actualImageById[4]);
        assertEquals('A', actualImageById[5]);
        assertEquals('A', actualImageById[18]);
        assertEquals('A', actualImageById[19]);
        assertEquals('A', actualImageById[20]);
        assertEquals('A', actualImageById[21]);
        assertEquals('A', actualImageById[22]);
        assertEquals('A', actualImageById[23]);
        verify(this.amazonS3).getObject(anyString(), anyString());
        verify(this.imageRepository).getImageById(anyLong());
        verify(s3Object).getObjectContent();
    }

    @Test
    public void testDeleteFileFromAmazonS3() throws SdkClientException {
        doNothing().when(this.amazonS3).deleteObject(anyString(), anyString());
        this.mediaService.deleteFileFromAmazonS3("foo.txt");
        verify(this.amazonS3).deleteObject(anyString(), anyString());
    }

    @Test
    public void testConvertImageEntityToImageDTO() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any()))
                .thenThrow(new TechnicalIssuesException("An error occurred"));
        assertThrows(TechnicalIssuesException.class, () -> this.mediaService.convertImageEntityToImageDTO(this.image));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertImageEntityToImageDTO2() {
        ImageDTO imageDTO = new ImageDTO(123L, "foo.txt");
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(imageDTO);
        assertSame(imageDTO, this.mediaService.convertImageEntityToImageDTO(this.image));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertVideoEntityToVideoDTO() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any()))
                .thenThrow(new TechnicalIssuesException("An error occurred"));
        assertThrows(TechnicalIssuesException.class, () -> this.mediaService.convertVideoEntityToVideoDTO(this.video));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertVideoEntityToVideoDTO2() {
        VideoDTO videoDTO = new VideoDTO(123L, "foo.txt");
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(videoDTO);
        assertSame(videoDTO, this.mediaService.convertVideoEntityToVideoDTO(this.video));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }
}

