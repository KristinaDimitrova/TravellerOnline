package traveller.model.POJOs;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.DTO.postDTO.RequestPostDTO;

import javax.persistence.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="posts")
public class Post {

    public static final int IMAGES_LIMIT = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private String latitude;
    private String longitude;
    private String description;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "id")
    private LocationType locationType;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private List<Image> images;


    private String videoUrl;

    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "users_like_posts",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "owner_id") }
    )
    @JsonManagedReference
    Set<User> likers = new HashSet<>();


    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "users_dislike_posts",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "owner_id") }
    )
    @JsonManagedReference
    Set<User>dislikers = new HashSet<>();

    public Post(RequestPostDTO postDTO) {
        this.createdAt = LocalDateTime.now();
        this.latitude = postDTO.getLatitude();
        this.longitude = postDTO.getLongitude();
        this.description = postDTO.getDescription();
    }

}




