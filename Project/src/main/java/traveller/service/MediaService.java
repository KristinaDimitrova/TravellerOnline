package traveller.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import traveller.exception.TechnicalIssuesException;
import traveller.model.dto.fileDTO.ImageDTO;
import traveller.model.dto.fileDTO.VideoDTO;
import traveller.model.pojo.Image;
import traveller.model.pojo.Video;
import traveller.repository.ImageRepository;
import traveller.repository.VideoRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Log4j2
@Service
public class MediaService {
    @Autowired
    private VideoRepository videoRepo;
    @Autowired
    private ImageRepository imageRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${application.bucket.name}")
    private String bucketName;
    @Autowired
    private AmazonS3 s3Client;

    public VideoDTO uploadVideo (MultipartFile videoFile){
        Video video = new Video();
        File file = convertMultiPartFileToFile(videoFile);
        String fileName = System.currentTimeMillis()+"_"+videoFile.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
        video.setFileName(fileName);
        videoRepo.save(video);
        file.delete();
        return convertVideoEntityToVideoDTO(video);
    }

    public byte[] getVideoById(long videoId){
        Video video = videoRepo.getVideoById(videoId);
        String fileName = video.getFileName();
        return  downloadFromAmazonS3(fileName);
    }

    public ImageDTO uploadImage(MultipartFile imageFile){
        System.out.println(":p");
        Image image = new Image();
        File file = convertMultiPartFileToFile(imageFile);
        String fileName = System.currentTimeMillis()+"_"+imageFile.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
        image.setFileName(fileName);
        imageRepo.save(image);
        file.delete();
        return convertImageEntityToImageDTO(image);
    }

    public byte[] getImageById(long imageId){
        Image image = imageRepo.getImageById(imageId);
        String fileName = image.getFileName();
        return downloadFromAmazonS3(fileName);
    }

    private byte[]downloadFromAmazonS3(String fileName){
        S3Object s3Object = s3Client.getObject(bucketName, fileName);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new TechnicalIssuesException();
        }
    }

    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }
        return convertedFile;
    }

    public void deleteFileFromAmazonS3(String fileName){
        s3Client.deleteObject(bucketName, fileName);
    }


    public ImageDTO convertImageEntityToImageDTO(Image image){
        return modelMapper.map(image, ImageDTO.class);
    }

    public VideoDTO convertVideoEntityToVideoDTO(Video video){
        return modelMapper.map(video, VideoDTO.class);
    }

    private boolean validateVideoType(){
        //todo
        return true;
    }

    private boolean validateImageType(){
        //todo
        return true;
    }

}