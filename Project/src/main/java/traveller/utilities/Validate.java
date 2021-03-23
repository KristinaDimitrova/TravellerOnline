package traveller.utilities;

import org.springframework.beans.factory.annotation.Autowired;
import traveller.exceptions.InvalidRegistrationInputException;
import traveller.model.dao.user.UserDBDao;

public class Validate {

    @Autowired
    private static UserDBDao userDao;

    public static void email(String email) throws InvalidRegistrationInputException {
        if(!ValidPattern.email(email)){
            throw new InvalidRegistrationInputException("Please enter a valid email.");
        }
        /*if(userDao.emailExists(email)){
            throw new InvalidRegistrationInputException("A Trivadu account already exists with this email address.");
        }*/
    }

   public static void username(String username) throws InvalidRegistrationInputException {
        if(!ValidPattern.username(username)){
            throw new InvalidRegistrationInputException("Please enter a valid username.");
        }
        /*if(userDao.usernameExists(username)){
            throw new InvalidRegistrationInputException("A Trivadu account already exists with this username.");
        }*/
    }

    //validate names
    public static void firstLastNames(String firstName, String lastName) throws InvalidRegistrationInputException{
        if(!ValidPattern.names(firstName, lastName)){
            throw new InvalidRegistrationInputException("Please enter valid first and last name.");
        }
    }


    public static void password(String password) throws InvalidRegistrationInputException {
        if(!ValidPattern.password(password)){
            //extra task:
            //validator can throw specific exception messages depending on the type of irregularity
            //e.g., Your password must be at least 6 characters long. Please try another.
            throw new InvalidRegistrationInputException("Please enter a strong password.");
        }
    }

    /*todo special task
    * appropriate message
     The validator must throw some specific exception depending on the type of mistake
      e.g. "Names on Facebook can't have characters from more than one alphabet."
    * */
}
