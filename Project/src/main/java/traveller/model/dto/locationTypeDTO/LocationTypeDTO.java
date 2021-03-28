package traveller.model.dto.locationTypeDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import traveller.model.POJOs.LocationType;

@Component
@Getter
@Setter
@NoArgsConstructor
public class LocationTypeDTO {
    private String name;

    public LocationTypeDTO(LocationType locationType){
        this.name = locationType.getName();
    }
}
