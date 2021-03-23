package traveller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import traveller.exceptions.BadRequestException;
import traveller.exceptions.InvalidRegistrationInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.exceptions.LoginException;
import traveller.exceptions.UnauthorizedException;
import traveller.model.DTO.LoginUserDTO;
import traveller.model.DTO.SignupUserDTO;
import traveller.model.POJOs.User;
import traveller.model.dao.user.UserDBDao;
import traveller.utilities.Validate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@RestController
public class UserController { //todo Moni

    @Autowired //=> turns it into singleton
    private UserDBDao userDao;

    @PostMapping(value="/singup") //fixme does not work
    public String register(@RequestBody SignupUserDTO dto, HttpServletResponse response) throws InvalidRegistrationInputException {
        Validate.firstLastNames(dto.getFirstName(), dto.getLastName());
        Validate.email(dto.getEmail());
        Validate.username(dto.getUsername());
        if(!dto.getRepeatedPassword().equals(dto.getPassword())){
            throw new InvalidRegistrationInputException("Passwords do not match.");
        }
        Validate.password(dto.getPassword());
        //todo encryptPassword()
        //User user = new User(); todo
        //userDao.insertUser(user); todo
        //todo mailService.sendRegistrationEmail(user);
        response.setStatus(200);
        response.setHeader("Yes", "Success");
        return "You're almost done! Please verify your email.";
    }

    @GetMapping(value="/search/{firstName}&{lastName}")
    public User getByName(@PathVariable String firstName, @PathVariable String lastName){
        return userDao.getByName(firstName, lastName);
    }

    private String encryptPassword(String password) {
        //todo use bcrypt
        return password;
    }

    @PostMapping(value="/login") //RequestParam АКО СЕ НАМИРА В формуляра за попълване
    public void logIn(@RequestBody LoginUserDTO loginUserDto, HttpSession session, HttpServletResponse resp) throws BadRequestException, LoginException, IOException {
        //does user by this id exist ?
        /*if(userDao.getById(userId) == null || !userDao.getById(userId).getPassword().matches(password)){
            throw new LoginException("Username and password do not match.");
        }*/
        String password = loginUserDto.getPassword();
        String username = loginUserDto.getUsername();
        resp.getWriter().append("Your username is " + loginUserDto.getUsername() + " and you password - " + password);
        if(SessionManager.isUserLoggedIn(session)){//ако вече е логнат
            throw new LoginException("Already logged in.");
        }
        if (username.isEmpty() || password.isEmpty()) {
            resp.setStatus(400);
            resp.getWriter().append("Email or password field is empty.");
            return;
        }
        try {
            User user = userDao.getByUsername(username);
            if (user == null || !user.getPassword().equals(loginUserDto.getPassword())) {
                resp.setStatus(400);
                resp.getWriter().append("Combination of password and username is incorrect.");
                return;
            }
            SessionManager.userLogsIn(session, user.getId());
        } catch (SQLException throwables) {
            resp.getWriter().append("Please try again later.");
        }

        //redirection to home page
        //resp.sendRedirect("http://localhost:7878/newsfeed"); //todo sends to newsfeed servlet

    }

    @PostMapping(value="/logout/{userId}") //id, session
    public void logOut(HttpSession session, @PathVariable("userId") String userId, HttpServletResponse resp) throws IOException, LoginException { //Path variable OR RequestBody User user
        //confirm that the person who is accessing this url is one with the same id
        if(!SessionManager.isUserLoggedIn(session)){
            throw new LoginException("You have already logged out.");
        }
        SessionManager.userLogsOut(session);
        resp.sendRedirect("http://localhost:7878/homepage"); //todo service
    }


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

    @DeleteMapping(value="/users/{id}/deleteAccount")
    public String deleteAccount(@PathVariable long id, HttpSession session) throws BadRequestException {
        //session must save user id
        //actor authentication -> are you the same person ?
        if(!SessionManager.isUserLoggedIn(session)){
            throw new
        }

        User user = null;
        user = userDao.getById(id);
        userDao.deleteUser(user);
        return "Trivadu will miss you. Hope to see you soon.";
    }

    //follow user todo
    @PostMapping(value="/users/{id}/follow")
    public String followUser(@PathVariable long id, HttpSession session){
        //do both users exist ?
        //session must save user id => extracting userId from session object
        long actorId = 12345; //delete me
        User follower = userDao.getById(actorId);
        User followed = userDao.getById()
    }

    @GetMapping(value="/users/{id}")
    public User getById(@PathVariable long id){
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

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(BadRequestException e){
        return "Sorry, bad request! -> " + e.getMessage();
    }

    @ExceptionHandler(InvalidRegistrationInputException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String handleException(InvalidRegistrationInputException e){
        return "We couldn't create an account for you. "  + e.getMessage();
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleException(LoginException e){
        return "Forbidden operation - " + e.getMessage();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleException(UnauthorizedException e){
        return "Unathorized operation - " + e.getMessage();
    }
}
