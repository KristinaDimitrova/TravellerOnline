package traveller.model.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;


@NoArgsConstructor
@Getter
@Setter
public class RequestPostDTO {
    @NotNull
    private String locationType;
    @NotNull
    private String latitude;
    @NotNull
    private String longitude;
    @NotNull
    private String description;
    @NotNull
    private ArrayList<Long> videoIds= new ArrayList<>();
    @NotNull
    private ArrayList<Long> imageIds = new ArrayList<>();


}
