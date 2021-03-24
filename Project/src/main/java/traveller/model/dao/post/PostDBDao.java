package traveller.model.dao.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import traveller.model.POJOs.Post;
import java.util.List;

@Component
public class PostDBDao implements PostDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Post> getNewsFeed(long id) {
        return null;
    }




}
