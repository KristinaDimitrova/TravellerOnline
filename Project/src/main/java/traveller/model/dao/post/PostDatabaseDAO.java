package traveller.model.dao.post;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import traveller.exception.BadRequestException;
import traveller.exception.TechnicalIssuesException;
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
@Log4j2
@Component
public class PostDatabaseDAO extends AbstractDao implements PostDAO {

    @Autowired
    private PostRepository postRepository;

    @Override
    public List<Post> getNewsFeed(long id, int page, int resultsPerPage)  {
        String sql = "SELECT p.id FROM users AS u \n" +
                "INNER JOIN posts AS p ON u.id = p.owner_id \n" +
                "INNER JOIN users_subscribe_users AS usu ON u.id = usu.subscribed_id \n" +
                "WHERE subscriber_id = ? \n" +
                "ORDER BY p.created_at DESC\n" +
                "LIMIT ? OFFSET ?";
        try(Connection c = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement ps = c.prepareStatement(sql);){
            ps.setLong(1, id);
            ps.setInt(2, resultsPerPage);
            ps.setInt(3, (page - 1) * resultsPerPage);
            ResultSet rs = ps.executeQuery();
            ArrayList<Long> ids = new ArrayList<>();
            while (rs.next()){
                long postId = rs.getLong(1);
                ids.add(postId);
            }

            return new ArrayList<>(postRepository.getPostsByIdIn(ids));
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
            throwables.printStackTrace();
            throw new TechnicalIssuesException();
        }
    }

    @Override
    public List<Post> filter(String username, String locationType) {
        try( Connection c = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection()){
            StringBuilder sql = new StringBuilder("SELECT p.id FROM posts AS p \n" +
                    "INNER JOIN users AS u \n" +
                    "ON p.owner_id = u.id\n" +
                    "INNER JOIN location_types AS lt \n" +
                    "ON p.location_type_id = lt.id\n" +
                    "LEFT OUTER JOIN users_like_posts AS ulp\n" +
                    "ON(p.id=ulp.post_id)");
            PreparedStatement ps;
            if(username.equals("") && locationType.equals("")){ //neither can be null because of @NotNull
                throw new BadRequestException("To fetch results, you must first select a filter.");
            }
            else
            if(locationType.equals("")){ //fetches all posts created by the user
                sql .append("WHERE u.username LIKE ?  \n" +
                        "ORDER BY  p.created_at DESC") ;
                ps = c.prepareStatement(sql.toString());
                ps.setString(1, "%"+username+"%");
            }
            else
            if(username.equals("")){ //fetches all posts with the same location tag
                sql.append("WHERE lt.name LIKE ?\n" +
                        "ORDER BY p.created_at DESC");
                ps = c.prepareStatement(sql.toString());
                ps.setString(1, "%"+locationType+"%");
            }
            else {
                sql.append("WHERE u.username LIKE ? AND lt.name LIKE ? \n" +
                        "ORDER BY p.created_at DESC") ;
                ps = c.prepareStatement(sql.toString());
                ps.setString(1, "%"+username+"%");
                ps.setString(2, "%"+locationType+"%");
            }
            ResultSet rs =  ps.executeQuery();
            ArrayList<Long> ids = new ArrayList<>();
            while (rs.next()){
                long postId = rs.getLong(1);
                System.out.println(postId);
                ids.add(postId);
            }
            rs.close();
            return new ArrayList<>(postRepository.getPostsByIdIn(ids));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error(throwables.getMessage());
            throw new TechnicalIssuesException();
        }
    }
}
