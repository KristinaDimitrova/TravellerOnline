package traveller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.fileDTO.VideoResponseDto;
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
    public String upload(@PathVariable(name="id") long postId, @RequestPart MultipartFile videoFile, HttpSession session){ //all bytes
        sessManager.authorizeLogin(session);
        //does post already contain another video ? => delete previous
        //create a physical file
        File physicalFile = new File(videoPath + File.separator + System.nanoTime() + ".mp4");
        //write all bytes from the multipart
        try(OutputStream os = new FileOutputStream(physicalFile)){
            os.write(videoFile.getBytes()); //напълни всички байтове във физ. файл
            //create a Video object
            Video vid = new Video();
            //set its url to the path of the physical file
            vid.setUrl(physicalFile.getAbsolutePath());
            vid.setPost(postRep.getPostById(postId)); //getPost
            //save Video object
            videoRep.save(vid);

        } catch (FileNotFoundException e) {
            e.printStackTrace();//tdood;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "strng"; //TODO finish and test
    }

    @GetMapping(value = "posts/{id}/video", produces = "video/*")
    public byte[] download(@PathVariable("id") long postId, HttpSession session){
        //TODO
        sessManager.authorizeLogin(session);
        //find the video from the database
        Post post = postRep.getPostById(postId);
        Video video = post.getVideo();
        //extract its url
        String url = video.getUrl();
        //get the physical file from the url
        File phyFile = new File(url);
        try {
            //read the bytes
            //write into response body
            return Files.readAllBytes(phyFile.toPath());
        } catch (IOException e) {
            throw new NotFoundException("Sorry, try again later.");
        }
    }

}
