package traveller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import traveller.exception.NotFoundException;
import traveller.model.pojo.Comment;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    default Comment getById(long id){
        Optional<Comment> opt = findById(id);
        if(!opt.isPresent()){
            throw new NotFoundException("Comment not found.");
        }
        return opt.get();
    }

    List<Comment> findCommentsByPost_Id(long postId);

    void deleteCommentById(long id);
}
