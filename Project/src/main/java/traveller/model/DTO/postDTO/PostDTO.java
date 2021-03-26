package traveller.model.DTO.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private String locationType;
    private String latitude;
    private String longitude;
    private String description;
}
