package traveller.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import traveller.model.POJOs.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByOwnerUsernameOrderByCreatedAt(String username);
    List<Post> findByLocationType_NameOrderByCreatedAt(String locationTypeName);
    void deletePost(long id);

}
