package traveller.model.dto.file;

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

    public VideoDTO(Video v){
        this.videoId = v.getId();
        this.videoFileName = v.getFileName();
    }

}
