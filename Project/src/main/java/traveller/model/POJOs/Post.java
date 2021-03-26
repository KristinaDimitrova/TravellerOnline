package traveller.model.POJOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//<<<<<<< HEAD
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

//=======
import javax.persistence.*;
//>>>>>>> c0b50d11bc6e4eb84187b069ec682629cb719384
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




