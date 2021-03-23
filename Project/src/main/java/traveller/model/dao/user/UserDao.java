package traveller.model.dao.user;

<<<<<<< Updated upstream
=======
import traveller.exceptions.BadRequestException;
>>>>>>> Stashed changes
import traveller.model.POJOs.User;

public interface UserDao {

    User insertUser(User user);
    User getByName(String firstName, String lastName);

    User getById(long id) throws BadRequestException; //return null if user does not exist => can be used to verify the existence of someone in db


    ArrayList<User> getAllUsers();
    User editUser(User u);
    void deleteUser(User u);
    void unfollow(User follower, User followed);
    void follow(User follower, User followed);

    boolean usernameExists(String username);
    boolean emailExists(String email);
}
