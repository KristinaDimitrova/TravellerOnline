package traveller.model.POJOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.DTO.userDTO.SignupUserDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @OneToMany(mappedBy = "owner")
    List<Post> posts;
    @ManyToMany //todo
    private List<User> followers;
    @ManyToMany //todo
    private List<User> followedUsers;
    @ManyToMany(mappedBy = "likers")
    private List<Post> likedPosts;
    @ManyToMany(mappedBy = "dislikers")
    private List<Post> dislikedPosts;

    public User(SignupUserDTO userDTO) {
        firstName = userDTO.getFirstName();
        lastName = userDTO.getLastName();
        email = userDTO.getEmail();
        username = userDTO.getUsername();
        password = userDTO.getPassword();
        createdAt = LocalDateTime.now();
    }

}
