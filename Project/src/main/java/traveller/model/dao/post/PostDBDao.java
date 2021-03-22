package traveller.model.dao.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PostDBDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
}
