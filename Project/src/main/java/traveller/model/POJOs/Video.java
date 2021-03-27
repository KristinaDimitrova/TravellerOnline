package traveller.model.POJOs;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
@Setter
@Getter
@Entity
@Table(name = "videos")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //middle table!!
    @OneToOne
    @JsonBackReference
    private Post post;
    private String url;


}
