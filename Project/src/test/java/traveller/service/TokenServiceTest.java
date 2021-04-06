package traveller.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.model.pojo.VerificationToken;
import traveller.registration.Role;
import traveller.repository.VerificationTokenRepository;

@ContextConfiguration(classes = {TokenService.class, User.class})
@ExtendWith(SpringExtension.class)
public class TokenServiceTest {
    @Autowired
    private User user;

    @Autowired
    private TokenService tokenService;

    @MockBean
    private VerificationTokenRepository verificationTokenRepository;

    @Test
    public void testFindByToken() {
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

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setId(123L);
        verificationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setToken("ABC123");
        verificationToken.setUser(user);
        when(this.verificationTokenRepository.findByToken(anyString())).thenReturn(verificationToken);
        assertSame(verificationToken, this.tokenService.findByToken("ABC123"));
        verify(this.verificationTokenRepository).findByToken(anyString());
    }

    @Test
    public void testFindByUser() {
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

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setId(123L);
        verificationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setToken("ABC123");
        verificationToken.setUser(user);
        when(this.verificationTokenRepository.findByUser((User) any())).thenReturn(verificationToken);
        assertSame(verificationToken, this.tokenService.findByUser(this.user));
        verify(this.verificationTokenRepository).findByUser((User) any());
    }

    @Test
    public void testSave() {
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

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setId(123L);
        verificationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setToken("ABC123");
        verificationToken.setUser(user);
        when(this.verificationTokenRepository.save((VerificationToken) any())).thenReturn(verificationToken);
        this.tokenService.save(new VerificationToken());
        verify(this.verificationTokenRepository).save((VerificationToken) any());
    }

    @Test
    public void testConfrimToken() {
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

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setConfirmedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setCreatedAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setId(123L);
        verificationToken.setExpiresAt(LocalDateTime.of(1, 1, 1, 1, 1));
        verificationToken.setToken("ABC123");
        verificationToken.setUser(user);
        when(this.verificationTokenRepository.findByToken(anyString())).thenReturn(verificationToken);
        assertEquals("Email confirmed.", this.tokenService.confrimToken("ABC123").getText());
        verify(this.verificationTokenRepository).findByToken(anyString());
    }
}

