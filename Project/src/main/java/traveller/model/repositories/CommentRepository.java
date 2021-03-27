package traveller.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import traveller.exceptions.NotFoundException;
import traveller.model.POJOs.Comment;

import java.util.List;
import java.util.Optional;

@Component
public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment getById(long id){
        Optional<Comment> opt = findById(id);
        if(!opt.isPresent()){
            throw new NotFoundException("Comment not found.");
        }
        return opt.get();
    }
    Optional<Comment> findById(long id);

    void deleteCommentById(long id);

    List<Comment> findCommentsByPost_Id(long postId);
}
