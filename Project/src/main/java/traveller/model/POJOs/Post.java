package traveller.model.POJOs;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class Post { //todo POJO Moni
    private int id;
    private String locationType;
    private String videoUrl;
    private User owner;
    private LocalDateTime dateCreated;
    private String latitude;
    private String longitude;
}
