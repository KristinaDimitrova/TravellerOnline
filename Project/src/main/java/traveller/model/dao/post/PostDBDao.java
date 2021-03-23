package traveller.model.dao.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import traveller.model.POJOs.Post;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class PostDBDao implements PostDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Post getPostById(int id) {
        return null;
    }

    @Override
    public List<Post> getNewsfeedOrderByParameter(String param, long userId) throws SQLException {
        String sql = "SELECT  ?  FROM  posts WHERE owner_id = ?";
        Connection c = jdbcTemplate.getDataSource().getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setString(1,param);
        ps.setLong(2, userId);
        return null;
    }

    @Override
    public List<Post> getPostsByParameterOrderByParam(String s1, String s2) {
        return null;
    }
}
