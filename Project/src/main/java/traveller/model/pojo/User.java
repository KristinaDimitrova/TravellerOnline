package traveller.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import traveller.model.dto.userDTO.SignupUserDTO;
import traveller.registration.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Component
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@EqualsAndHashCode //fixme test if redundant
@Entity
@Table(name="users")
public class User implements UserDetails {
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
    private int age; //TODO validate
    @Column
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    @Column
    private boolean enabled;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;


            //POSTS
    @OneToMany(mappedBy = "owner", cascade = { CascadeType.ALL })
    @JsonManagedReference
    List<Post> posts;
            //FOLLOWERS
    @ManyToMany(mappedBy = "followedUsers")
    private Set<User> followers;
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
    private Set<Post> likedPosts;
            //DISLIKED POSTS
    @ManyToMany(mappedBy = "dislikers", cascade = { CascadeType.ALL })
    private Set<Post> dislikedPosts;
            //COMMENTS
    @JsonBackReference
    @OneToMany(mappedBy = "owner", cascade = { CascadeType.ALL })
    private Set<Comment> comments;
            //LIKED COMMENTS
    @JsonBackReference
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "users_like_comments",
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="comment_id")})
    private Set<Comment> likedComments;

    public User(SignupUserDTO userDTO) {
        firstName = userDTO.getFirstName();
        lastName = userDTO.getLastName();
        email = userDTO.getEmail();
        username = userDTO.getUsername();
        password = userDTO.getPassword();
        createdAt = LocalDateTime.now();
        age = userDTO.getAge();
        enabled = false;
        posts = new ArrayList<>();
        role = Role.USER;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    //PUT is idempodent => calling it many times will result in the same outcome as calling it once

}
