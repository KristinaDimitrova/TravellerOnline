package traveller.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.SQLException;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.SearchDTO;
import traveller.model.dto.post.RequestPostDTO;
import traveller.model.dto.post.ResponsePostDTO;
import traveller.service.PostService;

@ContextConfiguration(classes = {PostController.class})
@ExtendWith(SpringExtension.class)
public class PostControllerTest {
    @Autowired
    private PostController postController;

    @MockBean
    private PostService postService;

    @MockBean
    private SessionManager sessionManager;

    @Test
    public void testEditPost() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.editPost(anyLong(), (RequestPostDTO) any(), anyLong())).thenReturn(new ResponsePostDTO());

        RequestPostDTO requestPostDTO = new RequestPostDTO();
        requestPostDTO.setLocationType("Location Type");
        requestPostDTO.setLongitude("Longitude");
        requestPostDTO.setImageIds(new ArrayList<Long>());
        requestPostDTO.setLatitude("Latitude");
        requestPostDTO.setDescription("The characteristics of someone or something");
        requestPostDTO.setVideoIds(new ArrayList<Long>());
        String content = (new ObjectMapper()).writeValueAsString(requestPostDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/posts/{id}", 123456789)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "{\"owner\":null,\"latitude\":null,\"longitude\":null,\"description\":null,\"locationType\":null,\"videos\":[],"
                                        + "\"images\":[],\"comments\":[],\"likes\":0,\"dislikes\":0}")));
    }

    @Test
    public void testCreatePost() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.addNewPost((RequestPostDTO) any(), anyLong())).thenReturn(new ResponsePostDTO());

        RequestPostDTO requestPostDTO = new RequestPostDTO();
        requestPostDTO.setLocationType("Location Type");
        requestPostDTO.setLongitude("Longitude");
        requestPostDTO.setImageIds(new ArrayList<Long>());
        requestPostDTO.setLatitude("Latitude");
        requestPostDTO.setDescription("The characteristics of someone or something");
        requestPostDTO.setVideoIds(new ArrayList<Long>());
        String content = (new ObjectMapper()).writeValueAsString(requestPostDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "{\"owner\":null,\"latitude\":null,\"longitude\":null,\"description\":null,\"locationType\":null,\"videos\":[],"
                                        + "\"images\":[],\"comments\":[],\"likes\":0,\"dislikes\":0}")));
    }

    @Test
    public void testDeletePost() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.deletePost(anyLong(), anyLong())).thenReturn(new MessageDTO("Text"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/posts/{id}", 123456789);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"Text\"}")));
    }

    @Test
    public void testLikePost() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.likePost(anyInt(), anyLong())).thenReturn(new MessageDTO("Text"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts/{id}/like", 123456789);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"Text\"}")));
    }

    @Test
    public void testUnlikePost() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.unlikePost(anyLong(), anyLong())).thenReturn(new MessageDTO("Text"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts/{id}/unlike", 123456789);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"Text\"}")));
    }

    @Test
    public void testDislikePost() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.dislikePost(anyLong(), anyLong())).thenReturn(new MessageDTO("Text"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts/{id}/dislike", 123456789);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"Text\"}")));
    }

    @Test
    public void testRemoveDislikeFromPost() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.removeDislikeFromPost(anyLong(), anyLong())).thenReturn(new MessageDTO("Text"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts/{id}/removeDislike", 123456789);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"Text\"}")));
    }

    @Test
    public void testFilter() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.filter((SearchDTO) any())).thenReturn(new ArrayList<ResponsePostDTO>());

        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setLocationType("Location Type");
        searchDTO.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(searchDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("[]")));
    }

    @Test
    public void testFilter2() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.filter((SearchDTO) any())).thenThrow(new SQLException());

        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setLocationType("Location Type");
        searchDTO.setName("Name");
        String content = (new ObjectMapper()).writeValueAsString(searchDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/posts/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers
                                .containsString("{\"text\":\"Sorry we are experiencing technical issues. Please try again later.\"}")));
    }

    @Test
    public void testGetById() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.getPostById(anyLong())).thenReturn(new ResponsePostDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/posts/{id}", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "{\"owner\":null,\"latitude\":null,\"longitude\":null,\"description\":null,\"locationType\":null,\"videos\":[],"
                                        + "\"images\":[],\"comments\":[],\"likes\":0,\"dislikes\":0}")));
    }

    @Test
    public void testGetNewsfeed() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.getNewsFeed(anyLong(), anyInt(), anyInt())).thenReturn(new ArrayList<ResponsePostDTO>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/posts/newsfeed/{page}", 123456789);
        MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("[]")));
    }

    @Test
    public void testGetNewsfeed2() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.postService.getNewsFeed(anyLong(), anyInt(), anyInt())).thenThrow(new SQLException());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/posts/newsfeed/{page}", 123456789);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.postController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(500))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers
                                .containsString("{\"text\":\"Sorry we are experiencing technical issues. Please try again later.\"}")));
    }
}

