package traveller.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import traveller.exception.NotFoundException;
import traveller.model.dto.fileDTO.ImageDTO;
import traveller.model.dto.fileDTO.VideoDTO;
import traveller.model.pojo.Image;
import traveller.model.pojo.Video;
import traveller.repository.ImageRepository;
import traveller.repository.VideoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

@Log4j2
@Service
public class MediaService {
    @Value("${file.path}")
    private String filePath;
    @Autowired
    private VideoRepository videoRepo;
    @Autowired
    private ImageRepository imageRepo;
    @Autowired
    private ModelMapper modelMapper;

    public VideoDTO uploadVideo(MultipartFile videoFile) {
        Video video = new Video();

        File physicalFile = new File(filePath + File.separator + System.nanoTime() + ".mp4");
        try (OutputStream os = new FileOutputStream(physicalFile)) {
            os.write(videoFile.getBytes());
            video.setUrl(physicalFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoRepo.save(video);
        return new VideoDTO(video);
    }


    public ImageDTO uploadImage(MultipartFile imageFile) {
        Image image  = new Image();
        File physicalFile = new File(filePath + File.separator + System.nanoTime() + ".png");
        try (OutputStream os = new FileOutputStream(physicalFile)) {
            os.write(imageFile.getBytes());
            image.setUrl(physicalFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageRepo.save(image);
        return new ImageDTO(image);
    }

    public byte[] getVideoById(long videoId) {
        Video video = videoRepo.getVideoById(videoId);
        String url = video.getUrl();
        File phyFile = new File(url);
        try {
            return Files.readAllBytes(phyFile.toPath());
        } catch (IOException e) {
            throw new NotFoundException("Sorry, problem with video downloading!");
        }
    }

    public byte[] getImageById(long imageId) {
        Image image = imageRepo.getImageById(imageId);
        String url = image.getUrl();
        File phyFile = new File(url);
        try {
            return Files.readAllBytes(phyFile.toPath());
        } catch (IOException e) {
            throw new NotFoundException("Sorry, problem with image downloading!");
        }
    }

    public Image convertImageDTOtoEntity(ImageDTO imageDTO){
        return modelMapper.map(imageDTO, Image.class);
    }

    public ImageDTO convertImageEntityToImageDTO(Image image){
        return modelMapper.map(image, ImageDTO.class);
    }

    public Video convertVideoDTOtoEntity(VideoDTO videoDTO){
        return modelMapper.map(videoDTO,Video.class);
    }

    public VideoDTO convertVideoEntityToImageDTO(Video video){
        return modelMapper.map(video, VideoDTO.class);
    }
}