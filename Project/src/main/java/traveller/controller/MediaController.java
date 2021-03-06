package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveller.model.dto.file.ImageDTO;
import traveller.model.dto.file.VideoDTO;
import traveller.service.MediaService;
import traveller.utilities.Validator;

import javax.servlet.http.HttpSession;

@RestController
public class MediaController extends AbstractController{

    private static final int RESULTS_PER_PAGE = 10;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private SessionManager sessionManager;

    @PutMapping( "/video")
    public VideoDTO uploadVideo(@RequestPart MultipartFile videoFile, HttpSession session){
        sessionManager.authorizeLogin(session);
        Validator.validateSizeOfFile(videoFile);
        Validator.validateItsVideo(videoFile.getContentType());
        return mediaService.uploadVideo( videoFile);
    }

    @PutMapping("/image")
    public ImageDTO uploadImage(@RequestPart MultipartFile imageFile, HttpSession session){
        sessionManager.authorizeLogin(session);
        String contentType = imageFile.getContentType();
        Validator.validateSizeOfFile(imageFile);
        Validator.validateItsImage(contentType); //todo
        return mediaService.uploadImage(imageFile);
    }

    @GetMapping(value = "/video/{id}", produces = "video/mp4")
    public byte[] getVideoById(@PathVariable(name="id") long videoId, HttpSession session){
        sessionManager.authorizeLogin(session);
        return mediaService.getVideoById(videoId);
    }

    @GetMapping (value = "/image/{id}", produces =  "image/*") // image/* fails the unit test!
    public byte[] getImageById(@PathVariable(name="id") long imageId, HttpSession session){
        sessionManager.authorizeLogin(session);
        return mediaService.getImageById(imageId);
    }
}
