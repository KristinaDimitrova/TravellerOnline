package traveller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traveller.exception.NotFoundException;
import traveller.model.pojo.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    default Post getPostById(long id) {
        Optional<Post> optionalPost = findById(id);
        if(optionalPost.isPresent()){
            return optionalPost.get();
        }
        else throw new NotFoundException("Post not found!");
    }

    List<Post> getPostsByIdIn(List<Long> ids);

}
