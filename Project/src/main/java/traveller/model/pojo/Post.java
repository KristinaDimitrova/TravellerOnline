package traveller.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name="posts")
public class Post {

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
    @JoinColumn(name = "location_type_id")
    private LocationType locationType;

    @JsonBackReference
    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User owner;

    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private List<Image> images = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private List<Video> videos = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "users_like_posts",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "owner_id") }
    )
    @JsonManagedReference
    private Set<User> likers = new HashSet<>();
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "users_dislike_posts",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "owner_id") }
    )
    @JsonManagedReference
    private Set<User>dislikers = new HashSet<>();







}




