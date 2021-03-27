package traveller.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traveller.exceptions.NotFoundException;
import traveller.model.POJOs.Post;
import traveller.model.POJOs.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

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
