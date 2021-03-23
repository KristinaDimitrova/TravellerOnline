package traveller.model.dao.user;

<<<<<<< Updated upstream
=======
import traveller.exceptions.BadRequestException;
>>>>>>> Stashed changes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import traveller.model.POJOs.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public User getById(long id) throws BadRequestException {
        return null;
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
}
