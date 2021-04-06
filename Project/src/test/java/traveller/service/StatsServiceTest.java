package traveller.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import traveller.exception.AuthorizationException;
import traveller.model.dao.statistics.StatsDBDao;
import traveller.model.pojo.Comment;
import traveller.model.pojo.Post;
import traveller.model.pojo.User;
import traveller.model.pojo.stats.StatsProfile;
import traveller.model.pojo.stats.StatsSignups;
import traveller.registration.Role;
import traveller.repository.UserRepository;

@ContextConfiguration(classes = {StatsDBDao.class, StatsService.class})
@ExtendWith(SpringExtension.class)
public class StatsServiceTest {
    @MockBean
    private StatsDBDao statsDBDao;

    @Autowired
    private StatsService statsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testGetSignupsCountByAgeRange() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.ADMIN); //must be the opposite
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
        assertThrows(AuthorizationException.class, () -> this.statsService.getSignupsCountByAgeRange(1, 3, 3, 123L));
        verify(this.userRepository).getById(anyLong());
    }

    @Test
    public void testGetSignupsCountByAgeRange2() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER); //for postman, must be the opposite
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
        StatsSignups statsSignups = new StatsSignups();
        when(this.statsDBDao.getSignupsCountByAgeRange(anyInt(), anyInt(), anyInt())).thenReturn(statsSignups);
        assertSame(statsSignups, this.statsService.getSignupsCountByAgeRange(1, 3, 3, 123L));
        verify(this.statsDBDao).getSignupsCountByAgeRange(anyInt(), anyInt(), anyInt());
        verify(this.userRepository).getById(anyLong());
    }

    @Test
    public void testGetFavouriteProfilesByAgeGroup() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.ADMIN); //must be USER, changed for postman
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
        assertThrows(AuthorizationException.class, () -> this.statsService.getFavouriteProfilesByAgeGroup(1, 3, 123L));
        verify(this.userRepository).getById(anyLong());
    }

    @Test
    public void testGetFavouriteProfilesByAgeGroup2() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER); //must be ADMIN
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
        when(this.statsDBDao.getFavouriteProfilesByAgeGroup(anyInt(), anyInt())).thenReturn(new ArrayList<StatsProfile>());
        assertTrue(this.statsService.getFavouriteProfilesByAgeGroup(1, 3, 123L).isEmpty());
        verify(this.statsDBDao).getFavouriteProfilesByAgeGroup(anyInt(), anyInt());
        verify(this.userRepository).getById(anyLong());
    }

    @Test
    public void testGetFavouriteProfilesByAgeGroup3() {
        User user = new User();
        user.setLastName("Doe");
        user.setRole(Role.USER); //must be ADMIN
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

        ArrayList<StatsProfile> statsProfileList = new ArrayList<StatsProfile>();
        statsProfileList.add(new StatsProfile(1, new User()));
        when(this.statsDBDao.getFavouriteProfilesByAgeGroup(anyInt(), anyInt())).thenReturn(statsProfileList);
        assertEquals(1, this.statsService.getFavouriteProfilesByAgeGroup(1, 3, 123L).size());
        verify(this.statsDBDao).getFavouriteProfilesByAgeGroup(anyInt(), anyInt());
        verify(this.userRepository).getById(anyLong());
    }
}

