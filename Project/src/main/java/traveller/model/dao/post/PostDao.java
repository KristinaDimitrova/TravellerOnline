package traveller.model.dao.post;

import traveller.model.POJOs.Post;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

public interface PostDao {

    List<Post> getNewsFeed(long id) throws SQLException;
    List<Post> filter(String name, String locationType) throws SQLException;



}
