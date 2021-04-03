package traveller.model.dto.fileDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.pojo.Video;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VideoDTO {
    private long videoId;
    private String videoFileName;

}
