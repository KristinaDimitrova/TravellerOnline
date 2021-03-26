package traveller.model.POJOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="posts")
public class Post {

    public Post( LocalDateTime createdAt, String latitude, String longitude) {
        this.createdAt = createdAt;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String videoUrl;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;
    private String latitude;
    private String longitude;
    @ManyToOne
    @JoinColumn(name = "location_type_id")
    private LocationType locationType;
    @ManyToOne
    @JoinColumn(name = "owner_id" )
    private User owner;
    @OneToMany(mappedBy = "post_id")
    private List<Image> images;
    @OneToMany(mappedBy = "post_id")
    private List<Comment> comments;
}




