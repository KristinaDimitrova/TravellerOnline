package traveller.controller;

import traveller.exceptions.InvalidRegistrationInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.POJOs.User;
import traveller.model.dao.user.UserDBDao;
import traveller.utilities.ValidPattern;


@RestController
public class UserController { //todo Moni

    @Autowired //=> turns it into singleton
    private UserDBDao userDao;

    public User register(User user) throws InvalidRegistrationInputException {
        validateFirstName(user.getFirstName());
        validateLastName(user.getLastName(), user.getFirstName());//fixme
        validateEmail(user.getEmail());
        validateUsername(user.getUsername());
        validatePassword(user.getPassword());
        //todo encryptPassword()
        return userDao.insertUser(user);
    }

    private String encryptPassword(String password) {
        //todo use bcrypt
        return password;
    }

    //register todo
    //validate email
    private void validateEmail(String email) throws InvalidRegistrationInputException {
        if(!ValidPattern.email(email)){
            throw new InvalidRegistrationInputException("Please enter a valid email.");
        }
        if(userDao.emailExists(email)){
            throw new InvalidRegistrationInputException("A Trivadu account already exists with this email address.");
        }
    }
    //validate username -> no spaces, no strange characters
    private void validateUsername(String username) throws InvalidRegistrationInputException {
        //correct pattern & username does not exist
        if(!ValidPattern.username(username)){
            throw new InvalidRegistrationInputException("Please enter a valid username.");
        }
        if(userDao.usernameExists(username)){
            throw new InvalidRegistrationInputException("A Trivadu account already exists with this username.");
        }
    }
    //validate names
    private void validateFirstName(String firstName) throws InvalidRegistrationInputException {
        if(!ValidPattern.name(firstName)){
            throw new InvalidRegistrationInputException("Please enter a valid first name.");
        }
    }

    private void validateLastName(String lastName, String firstName) throws InvalidRegistrationInputException {
        //todo has to check whether lastName uses the same alphabet
        if(!ValidPattern.name(lastName)){
            throw new InvalidRegistrationInputException("Please enter a valid last name.");
            //todo appropriate message
            //The validator must throw some specific exception depending on the type of mistake
            //e.g. "Names on Facebook can't have characters from more than one alphabet."
        }
    }
    //validate password -> has to be longer than 6 characters extra: strong password
    private void validatePassword(String password) throws InvalidRegistrationInputException {
        if(!ValidPattern.password(password)){ //todo Can controller classes throw exception?
            //extra task:
            //validator can throw specific exception messages depending on the type of irregularity
            //e.g., Your password must be at least 6 characters long. Please try another.
            throw new InvalidRegistrationInputException("Please enter a valid password.");
        }
    }

    //login todo

    //edit profile todo

    //delete todo

    //follow user todo

    //unfollow user todo

    //get by id todo

    //search by name todo


}
