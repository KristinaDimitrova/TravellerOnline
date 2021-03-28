package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveller.model.pojo.Image;
import traveller.model.repository.ImageRepository;
import traveller.model.repository.PostRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;



@RestController
public class ImageController {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Value("${file.path}")
    private String filePath;

    @PostMapping("/post/{id}/images")
    public Image upload(@PathVariable int id, @RequestPart MultipartFile file) throws IOException {
        File pFile = new File(filePath + File.separator + id+"_"+System.nanoTime() +".png");
        OutputStream os = new FileOutputStream(pFile);
        os.write(file.getBytes());
        Image image = new Image();
        image.setUrl(pFile.getAbsolutePath());
        image.setPost(postRepository.getPostById(id));
        imageRepository.save(image);
        os.close();
        return image;
    }

    @GetMapping(value = "/images/{id}", produces = "image/*")
    public byte[] download(@PathVariable long id) throws IOException {
        Image image = imageRepository.getImageById(id);
        String url = image.getUrl();
        File pFile = new File(url);
        return Files.readAllBytes(pFile.toPath());
    }

}
