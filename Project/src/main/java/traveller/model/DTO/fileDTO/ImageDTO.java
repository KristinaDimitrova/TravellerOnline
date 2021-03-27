package traveller.model.DTO.fileDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.POJOs.Image;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
    private long id;
    private String pictureUrl;

    public ImageDTO(Image picture) {
        this.id = picture.getId();
        this.pictureUrl = picture.getUrl();
    }
}

