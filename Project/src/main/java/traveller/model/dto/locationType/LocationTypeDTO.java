package traveller.model.dto.locationType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.pojo.LocationType;

@Component
@Getter
@Setter
@NoArgsConstructor
public class LocationTypeDTO {
    private long id;
    private String name;

    public LocationTypeDTO(LocationType locationType){
        this.id = locationType.getId();
        this.name = locationType.getName();
    }
}
