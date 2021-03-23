package traveller.model.dao.user;
import traveller.exceptions.BadRequestException;
import traveller.model.POJOs.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserDao {

    User insertUser(User user);
    User getByName(String firstName, String lastName) throws SQLException;
    User getByUsername(String username) throws SQLException;
    User getByEmail(String email) throws SQLException;
    User getById(long id) throws BadRequestException; //return null if user does not exist => can be used to verify the existence of someone in db
    ArrayList<User> getAllUsers();
    User editUser(User u);
    void deleteUser(User u);
    void unfollow(User follower, User followed);

    void follow(User follower, User followed);
    boolean usernameExists(String username);

    boolean emailExists(String email);
}
