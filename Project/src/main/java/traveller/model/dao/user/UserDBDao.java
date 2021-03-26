package traveller.model.dao.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserDBDao implements UserDao{ //todo

    @Autowired
    private JdbcTemplate jdbcTemplate;


}
