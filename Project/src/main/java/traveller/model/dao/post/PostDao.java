package traveller.model.dao.post;

import traveller.model.POJOs.Post;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

public interface PostDao {

    List<Post> getNewsFeed(long id);



}
