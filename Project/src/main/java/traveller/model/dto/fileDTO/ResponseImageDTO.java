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
    private byte [] image;

    public ResponseImageDTO(Image image) {
        String url = image.getUrl();
        File phyFile = new File(url);
        try {
            this.image = Files.readAllBytes(phyFile.toPath());
        } catch (IOException e) {
            throw new NotFoundException("Sorry, problem with image downloading!");
        }
    }
}

