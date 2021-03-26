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
        sessManager.authorizeLogin(session);
        sessManager.userLogsOut(session);
    }

    @PostMapping(value="users/edit")
    public UserWithoutPasswordDTO editProfile(HttpSession session, @PathVariable("id") long id,@RequestBody EditDetailsUserDTO requestDTO){
        long actorId = sessManager.authorizeLogin(session);
        return userService.changeDetails(actorId, requestDTO);
    }

    @PostMapping(value="users/change") //TODO postman
    public UserWithoutPasswordDTO changePassword(HttpSession session, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
                                 @RequestParam("repeatedNewPassword") String repeatedNewPassword)  {
        long actorId = sessManager.authorizeLogin(session);
        passwordMatches(repeatedNewPassword, newPassword);
        return userService.changePassword(actorId, oldPassword, newPassword);
    }

    private void passwordMatches(String passwordOne, String passwordTwo) throws BadRequestException {
        if(!passwordOne.equals(passwordTwo)){
            throw new BadRequestException("Passwords do not match.");
        }
    }

    @DeleteMapping(value="/users/deleteAccount")
    public void deleteAccount(HttpSession session, @PathVariable(name="id") long id) {
        long actorId = sessManager.authorizeLogin(session);
        theSameUser(id, actorId);
        userService.deleteUser(actorId);
        sessManager.userLogsOut(session);
    }

    @PostMapping(value="/users/{id}/follow")
    public void followUser(@PathVariable long id, HttpSession session) throws AuthorizationException, NotFoundException, BadRequestException {
        long actor = sessManager.authorizeLogin(session);
        notTheSameUser(actor, id);
        userService.followUser(actor, id);
    }

    @PostMapping(value="users/{id}/unfollow")
    public void unfollowUser(@PathVariable("id") long id, HttpSession session) throws AuthorizationException, NotFoundException, BadRequestException {
        long actor = sessManager.authorizeLogin(session);
        notTheSameUser(actor, id);
        userService.unfollowUser(actor, id);
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
