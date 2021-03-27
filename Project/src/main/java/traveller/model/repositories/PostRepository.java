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

    List<Post> findByOwnerUsernameOrderByCreatedAt(String username);
    List<Post> findByLocationType_NameOrderByCreatedAt(String locationTypeName);
    void deletePost(long id);
    Optional<Post> findById(long id);

    default
    Post getPostById(int id) {
        Optional<Post> optionalPost = findById(id);
        if(optionalPost.isPresent()){
            return optionalPost.get();
        }
        else throw new NotFoundException("Post not found!");
    }


}
