package traveller.model.dto.fileDTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import traveller.exception.NotFoundException;
import traveller.model.pojo.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseImageDTO {
    private long imageId;
    private String imageUrl;

    public ResponseImageDTO(Image image) {
      this.imageId = image.getId();
      this.imageUrl = image.getUrl();
    }
}

