package traveller.model.dao.user;



import traveller.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import traveller.model.POJOs.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Component
public class UserDBDao implements UserDao{ //todo

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public User insertUser(User user) {
        //dark magic ...
        return null;
    }

    @Override
    public User getByName(String firstName, String lastName) {
        return null;
    }

    @Override
    public User getByUsername(String username) throws SQLException {
        return null;
    }

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public User getById(long id) {
        // interacting with db
        //counts amount of users with this id : 0 or 1
        //if its 0, then returns null
        return null;
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return null;
    }

    @Override
    public User editUser(long userId) {
        return null;
    }

    @Override
    public void deleteUser(long userId) {

    }


    @Override
    public boolean unfollow(long followerId, long followedId) {
        return false;
    }

    @Override
    public boolean follow(long followerId, long followedId) {
        return false;
    }

    @Override
    public boolean usernameExists(String username) {
        String sqlUsername = "SELECT COUNT(id) FROM users WHERE username = ?"; //todo replace with hibernate
        try (PreparedStatement ps = null) { //connection.prepareStatement(sqlUsername) fixme
            ps.setString(1, username);
            ResultSet rowsReturned = ps.executeQuery();
            rowsReturned.next();
            return rowsReturned.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean emailExists(String email) {
        String sqlEmail = "SELECT COUNT(id) FROM users WHERE email = ?";//todo replace with hibernate
        try(PreparedStatement ps = null) { //connection.prepareStatement(sqlEmail) fixme
            ps.setString(1, email);
            ResultSet rowsReturned = ps.executeQuery();
            rowsReturned.next();
            return rowsReturned.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void changePassword(long userId, String newPassword) {

    }
}
