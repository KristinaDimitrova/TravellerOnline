package traveller.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashSet;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
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
import traveller.model.dto.user.EditDetailsUserDTO;
import traveller.model.dto.user.LoginUserDTO;
import traveller.model.dto.user.SignUpUserResponseDTO;
import traveller.model.dto.user.SignupUserDTO;
import traveller.model.dto.user.UserWithoutPasswordDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.registration.Role;
import traveller.service.UserService;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    @MockBean
    private SessionManager sessionManager;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    LocalDateTime time = LocalDateTime.of(1010, 10, 10, 10, 10);
    ArrayList<Post> posts = new ArrayList<>();
    private User user = new User(2, "Munka", "Ivanova", "email",
            "milka", "hardToGet1!", 24, time, true,
            false, Role.USER, posts, null, null,
            null, null, null, null);
    private Post post = new Post(5, time, "0.0", "0.0", "Thankful", null, null,
            null, null, null, null, null);
    private UserWithoutPasswordDTO dto = new UserWithoutPasswordDTO(user);

    @BeforeEach
    private void setup() throws Exception {
        posts.add(post);
    }

    @Test
    public void testFollowUser() throws Exception {
        when(this.userService.followUser(anyLong(), anyLong())).thenReturn(new MessageDTO("User followed"));
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/{id}/follow",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"User followed\"}")));
    }

    @Test
    public void testUnfollowUser() throws Exception {
        when(this.userService.unfollowUser(anyLong(), anyLong())).thenReturn(new MessageDTO("Unfollowed"));
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/{id}/unfollow",
                12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"Unfollowed\"}")));
    }

    @Test
    public void testChangePassword() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/password")
                //weak password => bad request
                .param("oldPassword", anyString())
                .param("newPassword", "foo")
                .param("repeatedNewPassword", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "{\"text\":\"Sorry,  Your password must contain at least one uppercase letter. Please try another.\"}")));
    }

    @Test
    public void testFindById() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(2L);
        when(this.userService.findById(anyLong())).thenReturn(dto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/{id}", 12345678987654321L);
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "{\"id\":2,\"firstName\":\"Munka\",\"lastName\":" +
                                        "\"Ivanova\",\"username\":\"milka\",\"email\":\"email\",\"age\"" +
                                        ":24,\"posts\":[]}")));
    }

    @Test
    public void testChangePassword2() throws Exception {
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/password")
                //false input password => bad request
                .param("oldPassword", anyString())
                .param("newPassword", "100")
                .param("repeatedNewPassword", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"text\":\"Sorry,  Passwords do not match.\"}")));
    }

    @Test
    public void testDeleteAccount() throws Exception {
        doNothing().when(this.userService).deleteUser(anyLong());
        doNothing().when(this.sessionManager).userLogsOut((javax.servlet.http.HttpSession) any());
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/users");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"text\":\"Account successfully deleted.\"}")));
    }

    @Test
    public void testEditProfile() throws Exception {
        when(this.userService.changeDetails(anyLong(), (EditDetailsUserDTO) any()))
                .thenReturn(new UserWithoutPasswordDTO());
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);

        EditDetailsUserDTO editDetailsUserDTO = new EditDetailsUserDTO();
        editDetailsUserDTO.setLastName("Doe");
        editDetailsUserDTO.setEmail("jane.doe@example.org");
        editDetailsUserDTO.setPassword("iloveyou");
        editDetailsUserDTO.setFirstName("Jane");
        String content = (new ObjectMapper()).writeValueAsString(editDetailsUserDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "{\"id\":0,\"firstName\":null,\"lastName\":null,\"username\":null,\"email\":null,\"age\":0,\"posts\":[]}")));
    }

    @Test
    public void testGetByName() throws Exception {
        when(this.userService.getUsersByName(anyString(), anyString())).thenReturn(new ArrayList<UserWithoutPasswordDTO>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users/search/{firstName}&{lastName}",
                "value", "value");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("[]")));
    }

    @Test
    public void testLogIn() throws Exception {
        when(this.sessionManager.isUserLoggedIn((javax.servlet.http.HttpSession) any())).thenReturn(true);

        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setPassword("iloveyou");
        loginUserDTO.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginUserDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"text\":\"Sorry,  Already logged in.\"}")));
    }

    @Test
    public void testLogIn2() throws Exception {
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
        when(this.userService.findByUsername(anyString())).thenReturn(user);
        when(this.userService.verifyLogin(anyString(), anyString())).thenReturn(new UserWithoutPasswordDTO());
        doNothing().when(this.sessionManager).userLogsIn((javax.servlet.http.HttpSession) any(), anyLong());
        when(this.sessionManager.isUserLoggedIn((javax.servlet.http.HttpSession) any())).thenReturn(false);

        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setPassword("iloveyou");
        loginUserDTO.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginUserDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "{\"id\":0,\"firstName\":null,\"lastName\":null,\"username\":null,\"email\":null,\"age\":0,\"posts\":[]}")));
    }

    @Test
    public void testLogIn3() throws Exception {
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
        when(this.userService.findByUsername(anyString())).thenReturn(user);
        when(this.userService.verifyLogin(anyString(), anyString())).thenReturn(new UserWithoutPasswordDTO());
        doNothing().when(this.sessionManager).userLogsIn((javax.servlet.http.HttpSession) any(), anyLong());
        when(this.sessionManager.isUserLoggedIn((javax.servlet.http.HttpSession) any())).thenReturn(false);

        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setPassword("");
        loginUserDTO.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(loginUserDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"text\":\"Sorry,  Username or password field must not be empty.\"}")));
    }

    @Test
    public void testLogIn4() throws Exception {
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
        when(this.userService.findByUsername(anyString())).thenReturn(user);
        when(this.userService.verifyLogin(anyString(), anyString())).thenReturn(new UserWithoutPasswordDTO());
        doNothing().when(this.sessionManager).userLogsIn((javax.servlet.http.HttpSession) any(), anyLong());
        when(this.sessionManager.isUserLoggedIn((javax.servlet.http.HttpSession) any())).thenReturn(false);

        LoginUserDTO loginUserDTO = new LoginUserDTO();
        loginUserDTO.setPassword("iloveyou");
        loginUserDTO.setUsername("");
        String content = (new ObjectMapper()).writeValueAsString(loginUserDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString("{\"text\":\"Sorry,  Username or password field must not be empty.\"}")));
    }

    @Test
    public void testLogOut() throws Exception {
        doNothing().when(this.sessionManager).userLogsOut((javax.servlet.http.HttpSession) any());
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users/logout");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"You logged out.\"}")));
    }

    @Test
    public void testLogOut2() throws Exception {
        doNothing().when(this.sessionManager).userLogsOut((javax.servlet.http.HttpSession) any());
        when(this.sessionManager.authorizeLogin((javax.servlet.http.HttpSession) any())).thenReturn(1L);
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/users/logout");
        postResult.contentType("Not all who wander are lost");
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(postResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("{\"text\":\"You logged out.\"}")));
    }

    @Test
    public void testRegister() throws Exception {
        when(this.userService.insertUser((SignupUserDTO) any())).thenReturn(new SignUpUserResponseDTO());

        SignupUserDTO signupUserDTO = new SignupUserDTO();
        signupUserDTO.setLastName("Doe");
        signupUserDTO.setEmail("jane.doe@example.org");
        signupUserDTO.setPassword("iloveyou");
        signupUserDTO.setUsername("janedoe");
        signupUserDTO.setRepeatedPassword("iloveyou");
        signupUserDTO.setAge(1);
        signupUserDTO.setFirstName("Jane");
        String content = (new ObjectMapper()).writeValueAsString(signupUserDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/singup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(this.userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(Matchers.containsString(
                                "{\"id\":0,\"age\":0,\"firstName\":null,\"lastName\":null,\"username\":null,\"email\":null}")));
    }
}

