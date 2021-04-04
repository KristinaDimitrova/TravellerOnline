package traveller.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.comment.CommentRequestDTO;
import traveller.model.dto.comment.CommentResponseDTO;
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

    @Test
    public void testCommentPost() throws Exception {
        when(this.sessionManager.authorizeLogin(any())).thenReturn(1L);
        when(this.commentService.addComment(anyLong(), any(), anyLong()))
                .thenReturn(new CommentResponseDTO());

        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setText("Random text");
        String content = (new ObjectMapper()).writeValueAsString(commentRequestDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/posts/{postId}/comments", 12345678987654321L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        initialiseMockMvcStandaloneReturningAComment(requestBuilder);
    }

    @Test
    public void testDelete() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.delete(anyLong(), anyLong())).thenReturn(new MessageDTO("Comment deleted"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/comments/{id}", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"Comment deleted\"}")));
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
                        .string(Matchers.containsString("{\"owner\":null,\"text\":null,\"createdAt\":null,\"likes\":0}")));
    }

    @Test
    public void testEdit() throws Exception { // FIXME: 4/3/2021
        when(this.sessionManager.authorizeLogin(any())).thenReturn(1L);
        when(this.commentService.editComment(anyLong(), (CommentRequestDTO) any(), anyLong()))
                .thenReturn(new CommentResponseDTO());

        CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setText("Text");
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
                        .string(Matchers.containsString("{\"owner\":null,\"text\":null,\"createdAt\":null,\"likes\":0}")));
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
    public void testLike() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        when(this.commentService.hitLike(anyLong(), anyLong())).thenReturn(new CommentResponseDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/comments/{id}/1", 12345678987654321L);
        initialiseMockMvcStandaloneReturningAComment(requestBuilder);
    }

    @Test
    public void testUnlike() throws Exception {
        when(this.sessionManager.authorizeLogin(any())).thenReturn(1L);
        when(this.commentService.removeLike(anyLong(), anyLong())).thenReturn(new CommentResponseDTO());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/comments/{id}/0", 12345678987654321L);
        initialiseMockMvcStandaloneReturningAComment(requestBuilder);
    }


    public void initialiseMockMvcStandaloneReturningAComment(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        MockMvcBuilders.standaloneSetup(this.commentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"owner\":null,\"text\":null,\"createdAt\":null,\"likes\":0}")));
    }


}

