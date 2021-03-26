package traveller.model.POJOs;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;

@Component
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "location_types")
public class LocationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    @JsonBackReference
    @OneToMany(mappedBy = "location_type_id")
    private List<Post> posts;
    public LocationType(String name){
        this.name = name;
    }

}