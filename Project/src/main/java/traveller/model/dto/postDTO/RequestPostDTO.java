package traveller.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;


@NoArgsConstructor
@Getter
@Setter
public class RequestPostDTO {
    private String locationType;
    private String latitude;
    private String longitude;
    private String description;
}
