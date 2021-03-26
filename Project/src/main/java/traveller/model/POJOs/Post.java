package traveller.model.POJOs;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.DTO.postDTO.PostDTO;

import javax.persistence.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Component
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="posts")
public class Post {

    public Post(PostDTO postDTO) {
        this.createdAt = LocalDateTime.now();
        this.latitude = postDTO.getLatitude();
        this.longitude = postDTO.getLongitude();
        this.description = postDTO.getDescription();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String videoUrl;
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
    @ManyToOne
    @JoinColumn(name = "owner_id" )
    private User owner;
    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private List<Image> images;
    @JsonManagedReference
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}




