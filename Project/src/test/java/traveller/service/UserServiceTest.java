package traveller.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import traveller.exception.AuthenticationException;
import traveller.exception.AuthorizationException;
import traveller.model.dto.user.EditDetailsUserDTO;
import traveller.model.dto.user.OwnerDTO;
import traveller.model.dto.user.SignUpUserResponseDTO;
import traveller.model.dto.user.SignupUserDTO;
import traveller.model.dto.user.UserWithoutPasswordDTO;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.registration.Role;
import traveller.repository.UserRepository;

@ContextConfiguration(classes = {ModelMapper.class, TokenService.class, EmailService.class, UserService.class,
        BCryptPasswordEncoder.class, User.class})
@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @Autowired
    private User user;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private EmailService emailService;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private UserWithoutPasswordDTO dtoNoPass;

    @Test
    public void testLoadUserByUsername() throws UsernameNotFoundException {
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
        when(this.userRepository.findByUsername(anyString())).thenReturn(user);
        assertSame(user, this.userService.loadUserByUsername("janedoe"));
        verify(this.userRepository).findByUsername(anyString());
    }

    @Test
    public void testGetUsersByName() {
        when(this.userRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(new ArrayList<User>());
        assertTrue(this.userService.getUsersByName("Jane", "Doe").isEmpty());
        verify(this.userRepository).findByFirstNameAndLastName(anyString(), anyString());
    }

    @Test
    public void testGetUsersByName2() {
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

        ArrayList<User> userList = new ArrayList<User>();
        userList.add(user);
        when(this.userRepository.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(userList);
        assertEquals(1, this.userService.getUsersByName("Jane", "Doe").size());
        verify(this.userRepository).findByFirstNameAndLastName(anyString(), anyString());
    }

    @Test
    public void testFindById() {
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
        when(this.userRepository.getById(anyLong())).thenReturn(user);
        UserWithoutPasswordDTO actualFindByIdResult = this.userService.findById(123L);
        assertEquals(1, actualFindByIdResult.getAge());
        assertEquals("janedoe", actualFindByIdResult.getUsername());
        assertEquals("Doe", actualFindByIdResult.getLastName());
        assertEquals(123L, actualFindByIdResult.getId());
        assertEquals("Jane", actualFindByIdResult.getFirstName());
        assertEquals("jane.doe@example.org", actualFindByIdResult.getEmail());
        verify(this.userRepository).getById(anyLong());
    }

    @Test
    public void testFindById2() {
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
        when(this.userRepository.getById(anyLong())).thenReturn(user);
        UserWithoutPasswordDTO actualFindByIdResult = this.userService.findById(123L);
        assertEquals(1, actualFindByIdResult.getAge());
        assertEquals("janedoe", actualFindByIdResult.getUsername());
        assertEquals("Doe", actualFindByIdResult.getLastName());
        assertEquals(123L, actualFindByIdResult.getId());
        assertEquals("Jane", actualFindByIdResult.getFirstName());
        assertEquals("jane.doe@example.org", actualFindByIdResult.getEmail());
        verify(this.userRepository).getById(anyLong());
    }

    @Test
    public void testDeleteUser() {
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
        doNothing().when(this.userRepository).delete((User) any());
        when(this.userRepository.getById(anyLong())).thenReturn(user);
        this.userService.deleteUser(123L);
        verify(this.userRepository).getById(anyLong());
        verify(this.userRepository).delete((User) any());
    }

    @Test
    public void testDeleteUser2() {
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
        doNothing().when(this.userRepository).delete((User) any());
        when(this.userRepository.getById(anyLong())).thenReturn(user);
        this.userService.deleteUser(123L);
        verify(this.userRepository).getById(anyLong());
        verify(this.userRepository).delete((User) any());
    }

    @Test
    public void testVerifyLogin() {
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
        when(this.userRepository.findByUsername(anyString())).thenReturn(user);
        when(this.bCryptPasswordEncoder.matches((CharSequence) any(), anyString())).thenReturn(true);
        UserWithoutPasswordDTO actualVerifyLoginResult = this.userService.verifyLogin("janedoe", "iloveyou");
        assertEquals(1, actualVerifyLoginResult.getAge());
        assertEquals("janedoe", actualVerifyLoginResult.getUsername());
        assertEquals("Doe", actualVerifyLoginResult.getLastName());
        assertEquals(123L, actualVerifyLoginResult.getId());
        assertEquals("Jane", actualVerifyLoginResult.getFirstName());
        assertEquals("jane.doe@example.org", actualVerifyLoginResult.getEmail());
        verify(this.bCryptPasswordEncoder).matches((CharSequence) any(), anyString());
        verify(this.userRepository).findByUsername(anyString());
    }

    @Test
    public void testVerifyLogin2() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER);
        user.setEmail("jane.doe@example.org");
        user.setPassword("iloveyou");
        user.setId(123L);
        user.setDeleted(true);
        user.setAge(1);
        user.setEnabled(false);
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
        when(this.userRepository.findByUsername(anyString())).thenReturn(user);
        when(this.bCryptPasswordEncoder.matches((CharSequence) any(), anyString())).thenReturn(true);
        assertThrows(AuthorizationException.class, () -> this.userService.verifyLogin("janedoe", "iloveyou"));
        verify(this.bCryptPasswordEncoder).matches((CharSequence) any(), anyString());
        verify(this.userRepository).findByUsername(anyString());
    }

    @Test
    public void testVerifyLogin3() {
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
        when(this.userRepository.findByUsername(anyString())).thenReturn(user);
        when(this.bCryptPasswordEncoder.matches((CharSequence) any(), anyString())).thenReturn(false);
        assertThrows(AuthenticationException.class, () -> this.userService.verifyLogin("janedoe", "iloveyou"));
        verify(this.bCryptPasswordEncoder).matches((CharSequence) any(), anyString());
        verify(this.userRepository).findByUsername(anyString());
    }

    @Test
    public void testChangePassword() {
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

        User user1 = new User();
        user1.setLastName("Doe");
        user1.setRole(Role.USER);
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setId(123L);
        user1.setDeleted(true);
        user1.setAge(1);
        user1.setEnabled(true);
        user1.setFirstName("Jane");
        user1.setPosts(new ArrayList<Post>());
        user1.setDislikedPosts(new HashSet<Post>());
        user1.setFollowedUsers(new ArrayList<User>());
        user1.setUsername("janedoe");
        user1.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        user1.setLikedPosts(new HashSet<Post>());
        user1.setLikedComment(new HashSet<Comment>());
        user1.setComments(new HashSet<Comment>());
        user1.setFollowers(new HashSet<User>());
        when(this.userRepository.save((User) any())).thenReturn(user1);
        when(this.userRepository.getById(anyLong())).thenReturn(user);
        when(this.bCryptPasswordEncoder.encode((CharSequence) any())).thenReturn("foo");
        when(this.bCryptPasswordEncoder.matches((CharSequence) any(), anyString())).thenReturn(true);
        assertEquals("Password changed.", this.userService.changePassword(123L, "iloveyou", "iloveyou").getText());
        verify(this.bCryptPasswordEncoder).encode((CharSequence) any());
        verify(this.bCryptPasswordEncoder).matches((CharSequence) any(), anyString());
        verify(this.userRepository).getById(anyLong());
        verify(this.userRepository).save((User) any());
    }

    @Test
    public void testChangePassword2() {
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

        User user1 = new User();
        user1.setLastName("Doe");
        user1.setRole(Role.USER);
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setId(123L);
        user1.setDeleted(true);
        user1.setAge(1);
        user1.setEnabled(true);
        user1.setFirstName("Jane");
        user1.setPosts(new ArrayList<Post>());
        user1.setDislikedPosts(new HashSet<Post>());
        user1.setFollowedUsers(new ArrayList<User>());
        user1.setUsername("janedoe");
        user1.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        user1.setLikedPosts(new HashSet<Post>());
        user1.setLikedComment(new HashSet<Comment>());
        user1.setComments(new HashSet<Comment>());
        user1.setFollowers(new HashSet<User>());
        when(this.userRepository.save((User) any())).thenReturn(user1);
        when(this.userRepository.getById(anyLong())).thenReturn(user);
        when(this.bCryptPasswordEncoder.encode((CharSequence) any())).thenReturn("foo");
        when(this.bCryptPasswordEncoder.matches((CharSequence) any(), anyString())).thenReturn(true);
        assertEquals("Password changed.", this.userService.changePassword(123L, "iloveyou", "iloveyou").getText());
        verify(this.bCryptPasswordEncoder).encode((CharSequence) any());
        verify(this.bCryptPasswordEncoder).matches((CharSequence) any(), anyString());
        verify(this.userRepository).getById(anyLong());
        verify(this.userRepository).save((User) any());
    }

    @Test
    public void testFollowUser() {
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

        User user1 = new User();
        user1.setLastName("Doe");
        user1.setRole(Role.USER);
        user1.setEmail("jane.doe@example.org");
        user1.setPassword("iloveyou");
        user1.setId(123L);
        user1.setDeleted(true);
        user1.setAge(1);
        user1.setEnabled(true);
        user1.setFirstName("Jane");
        user1.setPosts(new ArrayList<Post>());
        user1.setDislikedPosts(new HashSet<Post>());
        user1.setFollowedUsers(new ArrayList<User>());
        user1.setUsername("janedoe");
        user1.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        user1.setLikedPosts(new HashSet<Post>());
        user1.setLikedComment(new HashSet<Comment>());
        user1.setComments(new HashSet<Comment>());
        user1.setFollowers(new HashSet<User>());
        when(this.userRepository.save((User) any())).thenReturn(user1);
        when(this.userRepository.getById(anyLong())).thenReturn(user);
        assertEquals("User followed.", this.userService.followUser(1L, 1L).getText());
        verify(this.userRepository, times(2)).getById(anyLong());
        verify(this.userRepository).save((User) any());
    }

    @Test
    public void testFindByUsername() {
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
        when(this.userRepository.findByUsername(anyString())).thenReturn(user);
        assertSame(user, this.userService.findByUsername("janedoe"));
        verify(this.userRepository).findByUsername(anyString());
    }

    @Test
    public void testConvertUserEntityToSignUpResponseUserDto() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any()))
                .thenThrow(new AuthenticationException("An error occurred"));
        assertThrows(AuthenticationException.class,
                () -> this.userService.convertUserEntityToSignUpResponseUserDto(this.user));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertUserEntityToSignUpResponseUserDto2() {
        SignUpUserResponseDTO signUpUserResponseDTO = new SignUpUserResponseDTO();
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(signUpUserResponseDTO);
        assertSame(signUpUserResponseDTO, this.userService.convertUserEntityToSignUpResponseUserDto(this.user));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertUserEntityToSignUpResponseUserDto3() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any()))
                .thenThrow(new AuthenticationException("An error occurred"));
        assertThrows(AuthenticationException.class,
                () -> this.userService.convertUserEntityToSignUpResponseUserDto(this.user));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertUserEntityToSignUpResponseUserDto4() {
        SignUpUserResponseDTO signUpUserResponseDTO = new SignUpUserResponseDTO();
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(signUpUserResponseDTO);
        assertSame(signUpUserResponseDTO, this.userService.convertUserEntityToSignUpResponseUserDto(this.user));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertUserEntityToOwnerDto() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any()))
                .thenThrow(new AuthenticationException("An error occurred"));
        assertThrows(AuthenticationException.class, () -> this.userService.convertUserEntityToOwnerDto(this.user));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertUserEntityToOwnerDto2() {
        OwnerDTO ownerDTO = new OwnerDTO();
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(ownerDTO);
        assertSame(ownerDTO, this.userService.convertUserEntityToOwnerDto(this.user));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertUserEntityToOwnerDto3() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any()))
                .thenThrow(new AuthenticationException("An error occurred"));
        assertThrows(AuthenticationException.class, () -> this.userService.convertUserEntityToOwnerDto(this.user));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertUserEntityToOwnerDto4() {
        OwnerDTO ownerDTO = new OwnerDTO();
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(ownerDTO);
        assertSame(ownerDTO, this.userService.convertUserEntityToOwnerDto(this.user));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertSignUpUserDtoToUserEntity() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any()))
                .thenThrow(new AuthenticationException("An error occurred"));
        assertThrows(AuthenticationException.class,
                () -> this.userService.convertSignUpUserDtoToUserEntity(new SignupUserDTO()));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertSignUpUserDtoToUserEntity2() {
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
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(user);
        User actualConvertSignUpUserDtoToUserEntityResult = this.userService
                .convertSignUpUserDtoToUserEntity(new SignupUserDTO());
        assertSame(user, actualConvertSignUpUserDtoToUserEntityResult);
        assertFalse(actualConvertSignUpUserDtoToUserEntityResult.isEnabled());
        assertFalse(actualConvertSignUpUserDtoToUserEntityResult.isDeleted());
        assertEquals(Role.USER, actualConvertSignUpUserDtoToUserEntityResult.getRole());
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertSignUpUserDtoToUserEntity3() {
        User user = mock(User.class);
        doNothing().when(user).setPosts((java.util.List<traveller.model.pojo.Post>) any());
        doNothing().when(user).setEnabled(anyBoolean());
        doNothing().when(user).setRole((traveller.registration.Role) any());
        doNothing().when(user).setDeleted(anyBoolean());
        doNothing().when(user).setCreatedAt((java.time.LocalDateTime) any());
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(user);
        this.userService.convertSignUpUserDtoToUserEntity(new SignupUserDTO());
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
        verify(user).setPosts((java.util.List<traveller.model.pojo.Post>) any());
        verify(user).setDeleted(anyBoolean());
        verify(user).setEnabled(anyBoolean());
        verify(user).setCreatedAt((java.time.LocalDateTime) any());
        verify(user).setRole((traveller.registration.Role) any());
    }

    @Test
    public void testConvertSignUpUserDtoToUserEntity4() {
        when(this.modelMapper.map((Object) any(), (Class<Object>) any()))
                .thenThrow(new AuthenticationException("An error occurred"));
        assertThrows(AuthenticationException.class,
                () -> this.userService.convertSignUpUserDtoToUserEntity(new SignupUserDTO()));
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertSignUpUserDtoToUserEntity5() {
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
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(user);
        User actualConvertSignUpUserDtoToUserEntityResult = this.userService
                .convertSignUpUserDtoToUserEntity(new SignupUserDTO());
        assertSame(user, actualConvertSignUpUserDtoToUserEntityResult);
        assertFalse(actualConvertSignUpUserDtoToUserEntityResult.isEnabled());
        assertFalse(actualConvertSignUpUserDtoToUserEntityResult.isDeleted());
        assertEquals(Role.USER, actualConvertSignUpUserDtoToUserEntityResult.getRole());
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
    }

    @Test
    public void testConvertSignUpUserDtoToUserEntity6() {
        User user = mock(User.class);
        doNothing().when(user).setPosts((java.util.List<traveller.model.pojo.Post>) any());
        doNothing().when(user).setEnabled(anyBoolean());
        doNothing().when(user).setRole((traveller.registration.Role) any());
        doNothing().when(user).setDeleted(anyBoolean());
        doNothing().when(user).setCreatedAt((java.time.LocalDateTime) any());
        when(this.modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(user);
        this.userService.convertSignUpUserDtoToUserEntity(new SignupUserDTO());
        verify(this.modelMapper).map((Object) any(), (Class<Object>) any());
        verify(user).setPosts((java.util.List<traveller.model.pojo.Post>) any());
        verify(user).setDeleted(anyBoolean());
        verify(user).setEnabled(anyBoolean());
        verify(user).setCreatedAt((java.time.LocalDateTime) any());
        verify(user).setRole((traveller.registration.Role) any());
    }
}

