package traveller.model.dao.user;

import traveller.model.POJOs.User;

public interface UserDao {

    User insertUser(User user);
    /*
    User getById(int id) throws BadRequestException; //return null if user does not exist => can be used to verify the existence of someone in db
    ArrayList<User> getAllUsers();
    User editUser(User u);
    void deleteUser(User u);
    */
    boolean usernameExists(String username);
    boolean emailExists(String email);
}
