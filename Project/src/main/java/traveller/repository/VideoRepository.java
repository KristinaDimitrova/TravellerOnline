package traveller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traveller.exception.NotFoundException;
import traveller.model.pojo.Image;
import traveller.model.pojo.Video;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    default Video getVideoById(long id) {
        Optional<Video> optionalVideo = findById(id);
        if(optionalVideo.isPresent()){
            return optionalVideo.get();
        }
        else throw new NotFoundException("Image not found!");
    }
}
