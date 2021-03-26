package traveller.controller;

import traveller.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.DTO.*;
import traveller.model.dao.user.UserDBDao;
import traveller.model.repositoriesUser.UserRepository;
import traveller.service.UserService;
import traveller.utilities.Validate;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


@RestController
public class UserController { //todo Moni

    @Autowired //=> turns it into singleton
    private UserDBDao userDao;
    @Autowired //-> намира имплементация на интерфейса
    private UserRepository userRep;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessManager;


    @PostMapping(value="/singup") //todo postman
    public SignupResponseUserDTO register(@RequestBody SignupUserDTO dto) {
        return userService.insertUser(dto);
    }

    @GetMapping(value="/search/{firstName}&{lastName}") //todo postman
    public List<UserWithoutPasswordDTO> getByName(@PathVariable String firstName, @PathVariable String lastName){
        return userService.getUsersByName(firstName, lastName);
    }

    @PostMapping(value="/login") //RequestParam АКО СЕ НАМИРА В формуляра за попълване
    public UserWithoutPasswordDTO logIn(@RequestBody LoginUserDTO loginUserDTO, HttpSession session, HttpServletResponse resp){
        String password = loginUserDTO.getPassword();
        String username = loginUserDTO.getUsername();
        if(sessManager.isUserLoggedIn(session)){//ако вече е логнат
            throw new BadRequestException("Already logged in.");
        }
        if (username.isEmpty() || password.isEmpty()) {
            throw new BadRequestException("Username or password field is empty.");
        }
        return userService.loginWtUsername(username, password, session);
    }

    @PostMapping(value="/logout") //id, session
    public void logOut(HttpSession session) throws AuthorizationException { //Path variable OR RequestBody User user
        sessManager.userHasLoggedIn(session);
        sessManager.userLogsOut(session);
    }

    //edit profile todo
    @PostMapping(value="users/{id}/edit")
    public UserWithoutPasswordDTO editProfile(HttpSession session, @PathVariable("id") long id,@RequestBody EditDetailsUserDTO requestDTO){
        long actorId = sessManager.userHasLoggedIn(session);
        //theSameUser(id, actorId); potentially redundant
        //to make changes one must enter their password
        return userService.changeDetails(actorId, requestDTO);
    }

    @PostMapping(value="users/{id}/change")
    public String changePassword(HttpSession session, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
                                 @RequestParam("repeatedNewPassword") String repeatedNewPassword) throws AuthorizationException, InvalidRegistrationInputException, BadRequestException {
        long actor = sessManager.userHasLoggedIn(session);
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

    @DeleteMapping(value="/users/{id}/deleteAccount") //т.е. userId е на човека акаунта
    public void deleteAccount(HttpSession session, @PathVariable(name="id") long id) {
        long actorId = sessManager.userHasLoggedIn(session);
        theSameUser(id, actorId);
        userService.deleteUser(actorId);
        sessManager.userLogsOut(session);
    }

    @PostMapping(value="/users/{id}/follow")
    public String followUser(@PathVariable long id, HttpSession session) throws AuthorizationException, NotFoundException, BadRequestException {
        long actor = sessManager.userHasLoggedIn(session);
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
        long actor = sessManager.userHasLoggedIn(session);
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

    @GetMapping(value="/users/{id}") //todo postman
    public UserWithoutPasswordDTO findById(@PathVariable long id){
        return userService.findById(id);
    }

}
