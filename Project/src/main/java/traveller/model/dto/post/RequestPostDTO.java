package traveller.model.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@NoArgsConstructor
@Getter
@Setter
public class RequestPostDTO {
    private String locationType;
    private String latitude;
    private String longitude;
    private String description;
    private ArrayList<Long> videoIds= new ArrayList<>();
    private ArrayList<Long> imageIds = new ArrayList<>();


}
