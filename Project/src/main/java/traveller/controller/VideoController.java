package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveller.exceptions.BadRequestException;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.MessageDTO;
import traveller.model.POJOs.Post;
import traveller.model.POJOs.Video;
import traveller.model.repositories.PostRepository;
import traveller.model.repositories.VideoRepository;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;

@RestController
public class VideoController {
    @Autowired
    private SessionManager sessManager;
    @Value("${video.path")
    private String videoPath;
    @Autowired
    private VideoRepository videoRep;
    @Autowired
    private PostRepository postRep;

    @PutMapping(value = "/posts/{id}/video")
    public MessageDTO upload(@PathVariable(name="id") long postId, @RequestPart MultipartFile videoFile, HttpSession session){ //all bytes
        sessManager.authorizeLogin(session);
        Post post = postRep.getPostById(postId);
        if(post.getVideo() != null){
            throw new BadRequestException("You can upload only one video! ");
        }
        File physicalFile = new File(videoPath + File.separator + System.nanoTime() + ".mp4");
        try(OutputStream os = new FileOutputStream(physicalFile)){
            os.write(videoFile.getBytes());
            Video vid = new Video();
            vid.setUrl(physicalFile.getAbsolutePath());
            vid.setPost(post);
            videoRep.save(vid);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MessageDTO("Video uploaded successfully!"); //TODO  test
    }

    @GetMapping(value = "posts/{id}/video", produces = "video/*")
    public byte[] download(@PathVariable("id") long postId, HttpSession session){
        //TODO
        sessManager.authorizeLogin(session);
        Post post = postRep.getPostById(postId);
        Video video = post.getVideo();
        String url = video.getUrl();
        File phyFile = new File(url);
        try {
            return Files.readAllBytes(phyFile.toPath());
        } catch (IOException e) {
            throw new NotFoundException("Sorry, try again later.");
        }
    }

}
