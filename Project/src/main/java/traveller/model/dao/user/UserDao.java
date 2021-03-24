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
    User getById(long id); //return null if user does not exist => can be used to verify the existence of someone in db
    ArrayList<User> getAllUsers();
    User editUser(long userId);
    void deleteUser(long userId);
    /*Follow and unfollow methods return TRUE
      if the corresponding database table creates a new record
      of the action and return FALSE
      if the user has already performed this operation
    */
    boolean unfollow(long followerId, long followedId);
    boolean follow(long followerId, long followedId);
    boolean usernameExists(String username);

    boolean emailExists(String email);

    void changePassword(long userId, String newPassword);
}
