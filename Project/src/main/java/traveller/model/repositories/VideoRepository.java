package traveller.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import traveller.exceptions.NotFoundException;
import traveller.model.POJOs.Video;

import java.util.Optional;

@Component
public interface VideoRepository extends JpaRepository<Video, Long>{
    default Video getById(long id){
        Optional<Video> video = findById(id);
        if(!video.isPresent()){
            throw new NotFoundException("Video not found.");
        }
        return video.get();
    }
}
