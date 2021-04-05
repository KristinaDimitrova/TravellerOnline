package traveller.model.dto.file;


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
public class ImageDTO {
    private long imageId;
    private String imageFileName;

    public ImageDTO(Image i){
        this.imageId = i.getId();
        this.imageFileName = i.getFileName();
    }


}

