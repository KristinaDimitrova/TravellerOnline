package traveller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import traveller.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

import static traveller.controller.SessionManager.LOGGED_IN;


@RestController
public class UserController { //todo Moni

    @Autowired //=> turns it into singleton
    private UserDBDao userDao;

    @PostMapping(value="/singup")
    public String register(@RequestBody SignupUserDTO dto) throws InvalidRegistrationInputException {
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
    public void logIn(@RequestBody LoginUserDTO loginUserDto, HttpSession session, HttpServletResponse resp) throws LoginException, IOException {
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
        } catch (SQLException e) {
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
    public void editPassword(HttpServletRequest req, @RequestParam("old password") String oldPassword, @RequestParam("new password") String newPassword, @RequestParam("new password2") String newPasswordRep){
        if(req.getSession().isNew() || req.getAttribute("LOGGED") == null){
            //изритваш потребителя и му пращаш съобщение да се логне
        }
        //ако oldPassword не е вярна => Enter a valid password and try again

        //ako newPassword e <= 5 символа => Too short //todo Кога ми е нужно да слагам код в response?
        //ако !newPassword.equals(newPasswordRep) => Passwords do not match
    }

    @DeleteMapping(value="/users/{id}/deleteAccount")
    public String deleteAccount(@PathVariable long id, HttpSession session) throws UnauthorizedException {
        //session must save user id
        //actor authentication -> are you the same person ?
        if(!SessionManager.isUserLoggedIn(session)){
            throw new UnauthorizedException("You must log in first.");
        }

        User user = null;
        user = userDao.getById(id);
        userDao.deleteUser(user);
        return "Trivadu will miss you. Hope to see you soon.";
    }

    //follow user todo
    @PostMapping(value="/users/{id}/follow")
    public String followUser(@PathVariable long id, HttpSession session) throws UnauthorizedException, NotFoundException, BadRequestException {
        long actor = userHasLoggedInt(session);
        userExists(id);
        notTheSameUser(actor, id);
        if(userDao.follow(actor,id)){
            return "Followed";
        } else{ //this block emulates a 'follow' button.
            userDao.unfollow(actor, id);
            return "Unfollowed";
        }
    }

    private void notTheSameUser(long actor, long id) throws BadRequestException {
        if(actor == id){
            throw new BadRequestException("You cannot follow you own account.");
        }
    }

    @PostMapping(value="users/{id}/unfollow")
    public String unfollowUser(@PathVariable("id") long id, HttpSession session) throws UnauthorizedException, NotFoundException, BadRequestException {
        //do we have to validate the existence of both users ? (Here?)
        long actor = userHasLoggedInt(session);
        userExists(id);
        notTheSameUser(actor, id);
        if(userDao.unfollow(actor,id)){
            return "Unfollowed";
        } else{ //this block emulates a 'follow' button.
            userDao.follow(actor, id);
            return "Followed";
        }

    }

    private void userExists(long id) throws NotFoundException {
        if(userDao.getById(id) == null){
            throw new NotFoundException("User not found");
        }
    }

    private long userHasLoggedInt(HttpSession session) throws UnauthorizedException {
        if(!SessionManager.isUserLoggedIn(session)){
            throw new UnauthorizedException("You must log in first.");
        }
        return (Long) session.getAttribute(LOGGED_IN);
    }

    @GetMapping(value="/users/{id}")
    public User getById(@PathVariable long id){
        return userDao.getById(id);
    }
    //unfollow user todo


}
