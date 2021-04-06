package traveller.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import traveller.model.dto.comment.CommentRequestDTO;
import traveller.model.dto.comment.CommentResponseDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.registration.Role;
import traveller.service.CommentService;

@ContextConfiguration(classes = {CommentController.class})
@ExtendWith(SpringExtension.class)
public class CommentControllerTest {
    @Autowired
    private CommentController commentController;

    @MockBean
    private CommentService commentService;

    @MockBean
    private SessionManager sessionManager;

    private Comment comment;
    private Post post;
    private User user;
    private CommentResponseDTO dtoResp;
    private CommentRequestDTO dtoReq;

    @BeforeEach
    private void setup() throws Exception {
        LocalDateTime time = LocalDateTime.of(1010, 10, 10, 10, 10);
        post = new Post(5, time, "0.0", "0.0", "Thankful", null, null,
                null, null, null, null, null);
        ArrayList<Post> posts = new ArrayList<>();
        posts.add(post);
        user = new User(2, "Munka", "Ivanova", "email",
                "milka", "hardToGet1!", 24, time, true,
                false, Role.USER, posts, null, null,
                null, null, null, null);
        dtoReq = new CommentRequestDTO();
        dtoReq.setText("Nice weather.");
        comment = new Comment(4, user, "Nice weather.", time, post, null);

    }

    @Test
    public void testCommentPost() throws Exception {
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setText("Text");
        String content = (new ObjectMapper()).writeValueAsString(commentRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/posts/{postId}/comments", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"text\":\"There seems to be a problem with the input.\"}")));
    }

    @Test
    public void testCommentPost3() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.addComment(anyLong(), (CommentRequestDTO) any(), anyLong()))
                .thenReturn(new CommentResponseDTO());

        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setText("comments.U");
        String content = (new ObjectMapper()).writeValueAsString(commentRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/posts/{postId}/comments", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"id\":0,\"owner\":null,\"text\":null,\"createdAt\":null,\"likes\":0}")));
    }

    @Test
    public void testDelete() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.delete(anyLong(), anyLong())).thenReturn(new MessageDTO("Text"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/comments/{id}", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"Text\"}")));
    }

    @Test
    public void testGetById() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.getById(anyLong())).thenReturn(new CommentResponseDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/comments/{id}", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"id\":0,\"owner\":null,\"text\":null,\"createdAt\":null,\"likes\":0}")));
    }

    @Test
    public void testCommentPost2() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.addComment(anyLong(), (CommentRequestDTO) any(), anyLong()))
                .thenReturn(new CommentResponseDTO());

        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setText("Green valley.");

        String content = (new ObjectMapper()).writeValueAsString(commentRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/posts/{postId}/comments", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"id\":0,\"owner\":null,\"text\":null,\"createdAt\":null,\"likes\":0}")));
    }

    @Test
    @Disabled
    public void testEdit() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.editComment(anyLong(), (CommentRequestDTO) any(), anyLong()))
                .thenReturn(new CommentResponseDTO());

        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setText("Text");
        String content = (new ObjectMapper()).writeValueAsString(commentRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/comments/{id}", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"text\":\"There seems to be a problem with the input.\"}")));
    }

    @Test
    public void testEdit2() throws Exception {
        String text = "Passing with flying colors.";
        //POJOs

        Comment comment = new Comment();
        comment.setText(text);
        comment.setOwner(null);
        comment.setOwner(user);
        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setText(text);
        CommentResponseDTO dtoResponse = new CommentResponseDTO(comment);

        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.editComment(anyLong(), (CommentRequestDTO) any(), anyLong()))
                .thenReturn(dtoResponse);

        String content = (new ObjectMapper()).writeValueAsString(commentRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/comments/{id}", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"id\":0,\"owner\":{\"id\":2," +
                                "\"firstName\":\"Munka\",\"lastName\"" +
                                ":\"Ivanova\",\"username\":\"milka\"},\"text\":\"Passing with flying colors.\"" +
                                ",\"createdAt\":null,\"likes\":0}")));
    }

    @Test
    public void testGetCommentsByPostId() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.getComments(anyLong())).thenReturn(new ArrayList<CommentResponseDTO>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/posts/{id}/comments",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("[]")));
    }

    @Test
    public void testGetCommentsByPostId2() throws Exception {
        String text = "cute <3";
        Comment comment1 = new Comment();
        comment1.setText(text);
        comment1.setOwner(null);
        Post post = new Post();

        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.getComments(anyLong())).thenReturn(new ArrayList<CommentResponseDTO>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/posts/{id}/comments", 12345678987654321L);
        getResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("[]")));
    }

    @Test
    public void testLike() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.hitLike(anyLong(), anyLong())).thenReturn(new CommentResponseDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comments/{id}/1", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"id\":0,\"owner\":null,\"text\":null,\"createdAt\":null,\"likes\":0}")));
    }

    @Test
    public void testUnlike() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.removeLike(anyLong(), anyLong())).thenReturn(new CommentResponseDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comments/{id}/0", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"id\":0,\"owner\":null,\"text\":null,\"createdAt\":null,\"likes\":0}")));
    }
}

