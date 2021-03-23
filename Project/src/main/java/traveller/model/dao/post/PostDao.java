package traveller.model.dao.post;

import traveller.model.POJOs.Post;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

public interface PostDao {

    Post getPostById(int id);
    List<Post> getNewsfeedOrderByParameter(String param, long userId) throws SQLException;
    List<Post> getPostsByParameterOrderByParam(String s1, String s2);

}
