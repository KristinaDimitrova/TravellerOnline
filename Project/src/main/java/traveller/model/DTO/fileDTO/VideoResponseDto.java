package traveller.model.DTO.fileDTO;

import traveller.model.POJOs.Video;

public class VideoResponseDto {
    private String url;
    public VideoResponseDto(Video video) {
        url = video.getUrl();
    }
}

