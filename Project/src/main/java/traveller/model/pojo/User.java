package traveller.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.dto.userDTO.SignupUserDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private LocalDateTime createdAt;
            //POSTS
    @OneToMany(mappedBy = "owner", cascade = { CascadeType.ALL })
    @JsonManagedReference
    List<Post> posts;
            //FOLLOWERS
    @ManyToMany(mappedBy = "followedUsers")
    private List<User> followers;
            //FOLLOWED USERS
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "users_subscribe_users",
            joinColumns = {@JoinColumn(name="subscriber_id")},
            inverseJoinColumns = {@JoinColumn(name="subscribed_id")}
    )
    private List<User> followedUsers;
            //LIKED POSTS
    @ManyToMany(mappedBy = "likers", cascade = { CascadeType.ALL })
    private List<Post> likedPosts;
            //DISLIKED POSTS
    @ManyToMany(mappedBy = "dislikers", cascade = { CascadeType.ALL })
    private List<Post> dislikedPosts;
            //COMMENTS
    @JsonBackReference
    @OneToMany(mappedBy = "owner", cascade = { CascadeType.ALL })
    private List<Comment> comments;

    public User(SignupUserDTO userDTO) {
        firstName = userDTO.getFirstName();
        lastName = userDTO.getLastName();
        email = userDTO.getEmail();
        username = userDTO.getUsername();
        password = userDTO.getPassword();
        createdAt = LocalDateTime.now();
    }

    //PUT is idempodent => calling it many times will result in the same outcome as calling it once

}
