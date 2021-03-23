package traveller.model.POJOs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter

public class Post {
    @Id
    private long id;
    @Column
    private String locationType; //fixme това е foreign key в таблицата
    @Column
    private String videoUrl;
    //private User owner; fixme
    @Column
    private LocalDateTime createdAt;
    @Column
    private String latitude;
    @Column
    private String longitude;
}
