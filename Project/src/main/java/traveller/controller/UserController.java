package traveller.controller;

import traveller.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.dto.*;
import traveller.model.dto.userDTO.*;
import traveller.model.service.UserService;
import traveller.utilities.Validate;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@RestController
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessManager;

    @Transactional //FIXME Krasi, when do we have to use this annotation?
    @PostMapping(value="/singup")
    public MessageDTO register(@RequestBody SignupUserDTO dto) {
        return userService.insertUser(dto);
    }

    @GetMapping(value = "confirm/{token}")
    public MessageDTO confirm(@PathVariable(name ="token") String token){
        return userService.confrimToken(token);
    }

    @GetMapping(value="/search/{firstName}&{lastName}")
    public List<UserWithoutPasswordDTO> getByName(@PathVariable String firstName, @PathVariable String lastName){
        return userService.getUsersByName(firstName, lastName);
    }

    @PostMapping(value="/login") //RequestParam АКО СЕ НАМИРА В формуляра за попълване
    public UserWithoutPasswordDTO logIn(@RequestBody LoginUserDTO loginUserDTO, HttpSession session){
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

    @PostMapping(value="/logout")
    public MessageDTO logOut(HttpSession session) { //Path variable OR RequestBody User user
        sessManager.authorizeLogin(session);
        sessManager.userLogsOut(session);
        return new MessageDTO("You logged out.");
    }

    @PostMapping(value="/users/edit")
    public UserWithoutPasswordDTO editProfile(HttpSession session, @RequestBody EditDetailsUserDTO requestDTO){ //FIXME
        long actorId = sessManager.authorizeLogin(session);
        return userService.changeDetails(actorId, requestDTO);
    }

    @PostMapping(value="/users/change") //TODO postman
    public MessageDTO changePassword(HttpSession session, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
                                 @RequestParam("repeatedNewPassword") String repeatedNewPassword)  {
        long actorId = sessManager.authorizeLogin(session);
        passwordMatches(repeatedNewPassword, newPassword);
        Validate.passwordChange(newPassword);
        return userService.changePassword(actorId, oldPassword, newPassword);
    }

    private void passwordMatches(String passwordOne, String passwordTwo) {
        if(!passwordOne.equals(passwordTwo)){
            throw new BadRequestException("Passwords do not match.");
        }
    }

    @DeleteMapping(value="/users/delete")
    public MessageDTO deleteAccount(HttpSession session) {
        long actorId = sessManager.authorizeLogin(session);
        //theSameUser(id, actorId);
        userService.deleteUser(actorId);
        sessManager.userLogsOut(session);
        return new MessageDTO("Account successfully deleted.");
    }

    @PostMapping(value="/users/{id}/follow")
    public MessageDTO followUser(@PathVariable(name="id") long id, HttpSession session) {
        long actor = sessManager.authorizeLogin(session);
        notTheSameUser(actor, id);
        return userService.followUser(actor, id);
    }

    @PostMapping(value="/users/{id}/unfollow")
    public void unfollowUser(@PathVariable("id") long id, HttpSession session) {
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
            throw new AuthorizationException("You cannot make changes to another person's account.");
        }
    }

    @GetMapping(value="/users/{id}")
    public UserWithoutPasswordDTO findById(@PathVariable long id){
        return userService.findById(id);
    }

    //if changes don't work, this might be the reason -> https://youtu.be/n8z_Ds_zgP4?t=7376
}
