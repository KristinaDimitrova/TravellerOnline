package traveller.model.POJOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name="posts")
public class Post {

    public Post(String locationType, String videoUrl, long ownerId, LocalDateTime createdAt, String latitude, String longitude) {
        this.locationType = locationType;
        this.videoUrl = videoUrl;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Id
    private long id;
    @Column
    private String locationType; //fixme това е foreign key в таблицата
    @Column
    private String videoUrl;
    @Column
    private long ownerId;
    @Column
    private LocalDateTime createdAt;
    @Column
    private String latitude;
    @Column
    private String longitude;
}




