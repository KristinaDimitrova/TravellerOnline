package traveller.model.POJOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.DTO.SignupUserDTO;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Entity
@Table(name="users")
public class User { //todo Moni
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

    public User(SignupUserDTO userDTO) {
        firstName = userDTO.getFirstName();
        lastName = userDTO.getLastName();
        email = userDTO.getEmail();
        username = userDTO.getUsername();
        password = userDTO.getPassword();
        createdAt = LocalDateTime.now();
    }
}
