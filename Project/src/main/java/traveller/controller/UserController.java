package traveller.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import traveller.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.DTO.*;
import traveller.model.POJOs.User;
import traveller.model.dao.user.UserDBDao;
import traveller.model.repositoriesUser.UserRepository;
import traveller.service.UserService;
import traveller.utilities.Validate;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static traveller.controller.SessionManager.LOGGED_IN;


@RestController
public class UserController { //todo Moni

    @Autowired //=> turns it into singleton
    private UserDBDao userDao;
    @Autowired //-> намира имплементация на интерфейса
    private UserRepository userRep;
    @Autowired
    private UserService userService;


    @PostMapping(value="/singup") //todo postman
    public SignupResponseUserDTO register(@RequestBody SignupUserDTO dto) throws InvalidRegistrationInputException {
        Validate.firstLastNames(dto.getFirstName(), dto.getLastName());
        Validate.email(dto.getEmail());
        Validate.username(dto.getUsername());
        if(!dto.getRepeatedPassword().equals(dto.getPassword())){
            throw new InvalidRegistrationInputException("Passwords do not match.");
        }
        Validate.password(dto.getPassword());
        return userService.insertUser(dto);
    }

    @GetMapping(value="/search/{firstName}&{lastName}") //todo postman
    public List<UserWithoutPasswordDTO> getByName(@PathVariable String firstName, @PathVariable String lastName){
        return userService.getUsersByName(firstName, lastName);
    }

    @PostMapping(value="/login") //RequestParam АКО СЕ НАМИРА В формуляра за попълване
    public void logIn(@RequestBody LoginUserDTO loginUserDTO, HttpSession session, HttpServletResponse resp) throws IOException {
        String password = loginUserDTO.getPassword();
        String username = loginUserDTO.getUsername();
        resp.getWriter().append("Your username is " + loginUserDTO.getUsername() + " and you password - " + password);
        if(SessionManager.isUserLoggedIn(session)){//ако вече е логнат
            throw new BadRequestException("Already logged in.");
        }
        if (username.isEmpty() || password.isEmpty()) {
            throw new BadRequestException("Username or password field is empty.");
        }
        try {
            User user = userDao.getByUsername(username);
            if (user == null || !user.getPassword().equals(loginUserDTO.getPassword())) {
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

    @PostMapping(value="/logout") //id, session
    public void logOut(HttpSession session) throws IOException, AuthorizationException { //Path variable OR RequestBody User user
        userHasLoggedIn(session);
        SessionManager.userLogsOut(session);
    }


    //edit profile todo
        //make sure the person is logged in
        //make sure the input is valid

    @PostMapping(value="users/{id}/edit")
    public UserWithoutPasswordDTO editProfile(HttpSession session, @PathVariable("id") long id,@RequestBody EditDetailsUserDTO requestDTO){
        long actorId = userHasLoggedIn(session);
        theSameUser(id, actorId);
        //to make changes one must enter their password
        User user = userRep.getById(actorId);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(requestDTO.getPassword(), user.getPassword())){
            throw new AuthenticationException("Wrong password.");
        }
        //if first name is the same => it remains, the same for other details
        userService.changeDetails(actorId, requestDTO); //changes details that are different
        return userService.findById(actorId);
    }

    @PostMapping(value="users/{id}/change")
    public String changePassword(HttpSession session, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
                                 @RequestParam("repeatedNewPassword") String repeatedNewPassword) throws AuthorizationException, InvalidRegistrationInputException, BadRequestException {
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
    public void editUsername(HttpSession session, @RequestParam("newUsername") String newUsername, @RequestParam("password") String password) throws AuthorizationException {
        userHasLoggedIn(session);
        //new username is taken
        try {
            if (userDao.getByUsername(newUsername) != null);
        }catch(SQLException e){
            //todo
        }
        //password is incorrect
    }


    @DeleteMapping(value="/users/{id}/deleteAccount") //т.е. userId е на човека акаунта
    public String deleteAccount(HttpSession session, @PathVariable(name="id") long id) {
        long actorId = userHasLoggedIn(session);
        theSameUser(id, actorId);
        userService.deleteUser(actorId);
        SessionManager.userLogsOut(session);
        return "Account deleted.";
        //FIXME Krasi, what jason object should this method return?
    }

    @PostMapping(value="/users/{id}/follow")
    public String followUser(@PathVariable long id, HttpSession session) throws AuthorizationException, NotFoundException, BadRequestException {
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
    public String unfollowUser(@PathVariable("id") long id, HttpSession session) throws AuthorizationException, NotFoundException, BadRequestException {
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

    private void notTheSameUser(long actor, long id) {
        if(actor == id){
            throw new BadRequestException("You cannot follow you own account.");
        }
    }

    private void theSameUser(long id, long actorId) {
        if(actorId != id){
            throw new BadRequestException("You cannot make changes to another person's account.");
        }
    }

    private long userHasLoggedIn(HttpSession session){
        if(!SessionManager.isUserLoggedIn(session)){
            throw new AuthorizationException("You must log in first.");
        }
        return (Long) session.getAttribute(LOGGED_IN);
    }

    @GetMapping(value="/users/{id}") //todo postman
    public UserWithoutPasswordDTO findById(@PathVariable long id){
        return userService.findById(id);
    }

}
