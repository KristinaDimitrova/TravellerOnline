package traveller.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.model.POJOs.Image;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseImageDTO {
    private long id;
    private String pictureUrl;

    public ResponseImageDTO(Image picture) {
        this.pictureUrl = picture.getUrl();
    }
}

