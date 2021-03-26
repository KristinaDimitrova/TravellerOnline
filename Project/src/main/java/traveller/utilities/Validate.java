package traveller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import traveller.exceptions.InvalidRegistrationInputException;
import traveller.model.dao.user.UserDBDao;

public class Validate {

    public static void email(String email){
        if(!ValidPattern.email(email)){
            throw new InvalidRegistrationInputException("Please enter a valid email.");
        }
    }

   public static void username(String username){
        if(!ValidPattern.username(username)){
            throw new InvalidRegistrationInputException("Username must contain only Please enter a valid username.");
        }
    }

    public static void firstLastNames(String firstName, String lastName){
        if(!ValidPattern.names(firstName, lastName)){
            throw new InvalidRegistrationInputException("Please enter valid first and last name.");
        }
    }


    public static void password(String password){
        if(!ValidPattern.password(password)){
            throw new InvalidRegistrationInputException("Please enter a strong password.");
        }
    }
}
