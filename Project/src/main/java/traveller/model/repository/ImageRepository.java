package traveller.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traveller.exception.NotFoundException;
import traveller.model.pojo.Image;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    default Image getImageById(long id) {
        Optional<Image> optionalImage = findById(id);
        if(optionalImage.isPresent()){
            return optionalImage.get();
        }
        else throw new NotFoundException("Image not found!");
    }
}
