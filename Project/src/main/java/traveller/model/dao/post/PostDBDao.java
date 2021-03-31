package traveller.model.dao.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import traveller.exception.BadRequestException;
import traveller.model.dao.AbstractDao;
import traveller.model.pojo.Post;
import traveller.repository.PostRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class PostDBDao extends AbstractDao implements PostDao {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getNewsFeed(long id, int page, int resultsPerPage) throws SQLException {
        String sql = "SELECT * FROM users AS u\n" +
                "INNER JOIN posts AS p ON u.id = p.owner_id\n" +
                "INNER JOIN users_subscribe_users AS usu ON u.id = usu.subscribed_id\n" +
                "WHERE subscriber_id = ? \n" +
                "ORDER BY p.created_at\n" +
                "LIMIT ? OFFSET ?";
        List<Post> posts = new ArrayList<>();
        Connection c = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
        PreparedStatement ps = c.prepareStatement(sql);
        ps.setLong(1, id);
        ps.setInt(2, resultsPerPage);
        ps.setInt(3, (page - 1) * resultsPerPage);
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            long postId = rs.getLong(1);
            posts.add(postRepository.getPostById(postId));
        }
        return posts;
    }

    @Override
    public List<Post> filter(String name, String locationType) throws SQLException {
        List<Post> postList = new ArrayList<>();
        Connection c = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
        String sql = "SELECT p.id, COUNT(ulp.owner_id) AS likes FROM posts AS p \n" +
                "INNER JOIN users AS u \n" +
                "ON p.owner_id = u.id\n" +
                "INNER JOIN location_types AS lt \n" +
                "ON p.location_type_id = lt.id\n" +
                "LEFT OUTER JOIN users_like_posts AS ulp\n" +
                "ON(p.id=ulp.post_id)\n";
        PreparedStatement ps;
        if(name == null && locationType == null){
            throw new BadRequestException("At least one parameter is required!");
        }
        else
        if(locationType == null){
             sql += "WHERE u.username LIKE ?  \n" +
                    "ORDER BY likes DESC, p.created_at DESC";
            ps = c.prepareStatement(sql);
            ps.setString(1, name);
        }
        else
        if(name == null){
            sql += "WHERE lt.name LIKE ? \n" +
                    "ORDER BY likes DESC, p.created_at DESC";
            ps = c.prepareStatement(sql);
            ps.setString(1, locationType);
        }
        else {
           sql += "WHERE u.username LIKE ? AND lt.name LIKE ? \n" +
                    "ORDER BY likes DESC, p.created_at DESC";
            ps = c.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, locationType);
        }
        ResultSet rs =  ps.executeQuery();
        while (rs.next()){
            long postId = rs.getLong(1);
            postList.add(postRepository.getPostById(postId));
        }
        return postList;
    }


}
