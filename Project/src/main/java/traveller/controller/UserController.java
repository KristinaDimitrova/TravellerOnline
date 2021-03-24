package traveller.controller;

import traveller.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.DTO.LoginUserDTO;
import traveller.model.DTO.SignupUserDTO;
import traveller.model.POJOs.User;
import traveller.model.dao.user.UserDBDao;
import traveller.utilities.ValidPattern;
import traveller.utilities.Validate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

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
            resp.getWriter().append("Username or password field is empty.");
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
        //resp.sendRedirect("http://localhost:7878/newsfeed"); //must send to newsfeed servlet todo

    }

    @PostMapping(value="/logout/{userId}") //id, session
    public void logOut(HttpSession session, @PathVariable("userId") String userId, HttpServletResponse resp) throws IOException, UnauthorizedException { //Path variable OR RequestBody User user
        userHasLoggedIn(session);
        SessionManager.userLogsOut(session);
        //resp.sendRedirect("http://localhost:7878/homepage"); //todo service
    }


    //edit profile todo
        //make sure the person is logged in
        //make sure the input is valid

    @PostMapping(value="/edit/password")
    @ResponseBody //това казва на Spring, че ние ще определим какъв ще е отговора
    public String editPassword(HttpSession session, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
                             @RequestParam("repeatedNewPassword") String repeatedNewPassword) throws UnauthorizedException, InvalidRegistrationInputException, BadRequestException {
        long actor = userHasLoggedIn(session);
        //check user's password
        passwordMatches(oldPassword, userDao.getById(actor).getPassword());
        Validate.password(newPassword);
        userDao.changePassword(actor, newPassword);
        //todo optional send email with notification
        return "Password changed.";
        //todo Кога ми е нужно да слагам код в response и кога мога да си хвърля Exception?
    }

    private void passwordMatches(String passwordOne, String passwordTwo) throws BadRequestException {
        if(!passwordOne.equals(passwordTwo)){
            throw new BadRequestException("Passwords must match.");
        }
    }

    @PostMapping(value="/edit/username")
    public void editUsername(HttpSession session, @RequestParam("newUsername") String newUsername, @RequestParam("password") String password) throws UnauthorizedException {
        userHasLoggedIn(session);
        //new username is taken
        try {
            if (userDao.getByUsername(newUsername) != null);
        }catch(SQLException e){
            //todo
        }
        //password is incorrect
    }


    @DeleteMapping(value="/users/deleteAccount")
    public String deleteAccount(HttpSession session) throws UnauthorizedException {
        long user = userHasLoggedIn(session);
        userDao.deleteUser(user);
        return "Trivadu will miss you. Hope to see you soon.";
    }

    @PostMapping(value="/users/{id}/follow")
    public String followUser(@PathVariable long id, HttpSession session) throws UnauthorizedException, NotFoundException, BadRequestException {
        long actor = userHasLoggedIn(session);
        userExists(id);
        notTheSameUser(actor, id);
        if(userDao.follow(actor,id)){
            return "Followed";
        } else{ //this block emulates a 'follow' button.
            userDao.unfollow(actor, id);
            return "Unfollowed";
        }
    }

    @PostMapping(value="users/{id}/unfollow")
    public String unfollowUser(@PathVariable("id") long id, HttpSession session) throws UnauthorizedException, NotFoundException, BadRequestException {
        //do we have to validate the existence of both users ? (Here?)
        long actor = userHasLoggedIn(session);
        userExists(id);
        notTheSameUser(actor, id);
        if(userDao.unfollow(actor,id)){
            return "Unfollowed";
        } else{         //this block emulates a 'follow' button.
            userDao.follow(actor, id);
            return "Followed";
        }
    }

    private void userExists(long id) throws NotFoundException {
        if(userDao.getById(id) == null){
            throw new NotFoundException("User not found");
        }
    }

    private void notTheSameUser(long actor, long id) throws BadRequestException {
        if(actor == id){
            throw new BadRequestException("You cannot follow you own account.");
        }
    }

    private long userHasLoggedIn(HttpSession session) throws UnauthorizedException {
        if(!SessionManager.isUserLoggedIn(session)){
            throw new UnauthorizedException("You must log in first.");
        }
        return (Long) session.getAttribute(LOGGED_IN);
    }

    @GetMapping(value="/users/{id}")
    public User getById(@PathVariable long id){
        return userDao.getById(id);
    }

}
