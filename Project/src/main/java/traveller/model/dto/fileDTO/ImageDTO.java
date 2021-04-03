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
public class ImageDTO {
    private long imageId;
    private String imageFileName;

}

