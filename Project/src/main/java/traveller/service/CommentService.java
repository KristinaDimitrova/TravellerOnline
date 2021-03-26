package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import traveller.model.repositoriesUser.CommentRepository;

@Component
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

}
