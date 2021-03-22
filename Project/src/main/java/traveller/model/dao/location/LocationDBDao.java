package traveller.model.dao.location;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LocationDBDao {
    private JdbcTemplate jdbcTemplate;
}
