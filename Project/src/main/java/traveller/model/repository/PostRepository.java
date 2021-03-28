package traveller.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import traveller.exceptions.NotFoundException;
import traveller.model.pojo.Post;

import java.util.Optional;

@Repository
@Component
public interface PostRepository extends JpaRepository<Post,Long> {

    void deletePost(long id);
    Optional<Post> findById(long id);

    default Post getPostById(long id) {
        Optional<Post> optionalPost = findById(id);
        if(optionalPost.isPresent()){
            return optionalPost.get();
        }
        else throw new NotFoundException("Post not found!");
    }


}
