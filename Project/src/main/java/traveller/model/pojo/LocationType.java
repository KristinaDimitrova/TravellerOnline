package traveller.model.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.dto.locationType.LocationTypeDTO;

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
    @OneToMany(mappedBy = "locationType")
    private List<Post> posts;
    public LocationType(String name){
        this.name = name;
    }

    public LocationType(LocationTypeDTO locationTypeDTO){
        this.name = locationTypeDTO.getName();
    }

}
