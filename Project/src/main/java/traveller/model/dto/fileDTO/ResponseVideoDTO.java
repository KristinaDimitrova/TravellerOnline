package traveller.model.dto.fileDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.pojo.Image;
import traveller.model.pojo.Video;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseVideoDTO {
    private long imageId;
    private String imageUrl;

    public ResponseVideoDTO(Video video) {
        this.imageId = video.getId();
        this.imageUrl = video.getUrl();
    }
}
