package traveller.model.dto.postDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@NoArgsConstructor
@Getter
@Setter
public class RequestPostDTO {
    private long id;
    private String locationType;
    private String latitude;
    private String longitude;
    private String description;
    private ArrayList<Long> videoIds= new ArrayList<>();
    private ArrayList<Long> imageIds = new ArrayList<>();


}
