package traveller.controller;

<<<<<<< Updated upstream
=======
import traveller.exceptions.BadRequestException;
>>>>>>> Stashed changes
import traveller.exceptions.InvalidRegistrationInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import traveller.model.POJOs.User;
import traveller.model.dao.user.UserDBDao;
import traveller.utilities.ValidPattern;
<<<<<<< Updated upstream
=======

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
>>>>>>> Stashed changes


@RestController
public class UserController { //todo Moni

    @Autowired //=> turns it into singleton
    private UserDBDao userDao;

    @GetMapping(value="/accounts/signup") //todo test
    public String register(@RequestBody User user, BindingResult bindingResult) throws InvalidRegistrationInputException { //fixme exception
        if(bindingResult.hasErrors()){//todo test if it works
            return"Sorry, try again later";
        }else {
            validateFirstName(user.getFirstName());
            validateLastName(user.getLastName(), user.getFirstName());
            validateEmail(user.getEmail());
            validateUsername(user.getUsername());
            validatePassword(user.getPassword());
            //todo encryptPassword()
            userDao.insertUser(user);
            //todo outcome from successful registration
            //& outcome from failure
            return "New registration created.";
        }
    }

    @GetMapping(value="/search?{firstName}{lastName}") //todo test PathVariable АКО СЕ НАМИРА В URL-a
    public User getByName(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName){
        return userDao.getByName(firstName, lastName);
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
    @PostMapping(value="/login/{userId}") //RequestParam АКО СЕ НАМИРА В формуляра за попълване
    public void logIn(@RequestBody User user, HttpSession session, @RequestParam("username") String username,
                      @RequestParam("password") String password){

        if(userDao.getById(user.getId()) == null || !userDao.getById(user.getId()).getPassword().matches(password)){
            //username and password do not match
        }

        if(SessionManager.validateLogin(session)){
            //ако вече е логнат
        } else{
            SessionManager.userLogsIn(session, user);
            //redirection to home page
        }
    }

    @PostMapping(value="/exit/{userId}")
    public void

    //edit profile todo
        //make sure the person is logged in
        //make sure the input is valid
    @PostMapping(value="/edit/password")
    @ResponseBody //това казва на Spring, че ние ще определим какъв ще е отговора
    public void editPassword(HttpServletRequest req, HttpServletResponse resp, @RequestParam("old password") String oldPassword, @RequestParam("new password") String newPassword, @RequestParam("new password2") String newPasswordRep){
        if(req.getSession().isNew() || req.getAttribute("LOGGED") == null){
            //изритваш потребителя и му пращаш съобщение да се логне
        }
        //ако oldPassword не е вярна => Enter a valid password and try again

        //ako newPassword e <= 5 символа => Too short //todo Кога ми е нужно да слагам код в response?
        //ако !newPassword.equals(newPasswordRep) => Passwords do not match
    }


    //delete todo

    //follow user todo


    @GetMapping(value="/users/{id}")
    public User getById(@PathVariable("id") String id){
        //get by id todo
        long idNum = Long.valueOf(id);
        try {
            return userDao.getById(idNum);
        }catch (BadRequestException e) {
            //outcome to the response
        }
        return null;

    }

    //unfollow user todo
    @GetMapping(value="users/{id}/unfollow")
    public void unfollowUser(@PathVariable("id") String id){
        long idUnfollowed = Long.valueOf(id);
        //has to validate the existence of this user
        //has to validate the existence of the other user
        //has to validate session attribute Logged in

        //userDao.unfollow()fixme

    }

    //todo user is logged => session manager


}
