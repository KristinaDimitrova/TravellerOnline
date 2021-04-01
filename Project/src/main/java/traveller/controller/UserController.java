package traveller.controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import traveller.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import traveller.model.dto.*;
import traveller.model.dto.userDTO.*;
import traveller.service.UserService;
import traveller.utilities.Validator;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController extends AbstractController{

    @Autowired
    private UserService userService;
    @Autowired
    private SessionManager sessManager;

    @PostMapping(value="/singup")
    public SignUpUserResponseDTO register(@Valid @RequestBody SignupUserDTO dto) {
        return userService.insertUser(dto);
    }

    @GetMapping(value="/users/search/{firstName}&{lastName}")
    public List<UserWithoutPasswordDTO> getByName(@PathVariable String firstName, @PathVariable String lastName){
        return userService.getUsersByName(firstName, lastName);
    }

    @PostMapping(value="/users/login") //RequestParam АКО СЕ НАМИРА В формуляра за попълване
    public UserWithoutPasswordDTO logIn(@RequestBody LoginUserDTO loginUserDTO, HttpSession session){
        String password = loginUserDTO.getPassword();
        String username = loginUserDTO.getUsername();
        if(sessManager.isUserLoggedIn(session)){//ако вече е логнат
            throw new BadRequestException("already logged in.");
        }
        if (username.isEmpty() || password.isEmpty()) {
            throw new BadRequestException("username or password field is blank.");
        }
        UserWithoutPasswordDTO dtoResponse =  userService.verifyLogin(username, password);
        sessManager.userLogsIn(session, userService.findByUsername(username).getId());
        return dtoResponse;
    }

    @PostMapping(value="/users/logout")
    public MessageDTO logOut(HttpSession session) { //Path variable OR RequestBody User user
        System.out.println("Has the user logged in?");
        sessManager.authorizeLogin(session);
        System.out.println("The user has logged in.");
        sessManager.userLogsOut(session);
        System.out.println("The user has logged OUT");
        return new MessageDTO("You logged out.");
    }

    @PostMapping(value="/users")
    public UserWithoutPasswordDTO editProfile(HttpSession session, @RequestBody EditDetailsUserDTO requestDTO){
        long actorId = sessManager.authorizeLogin(session);
        return userService.changeDetails(actorId, requestDTO);
    }

    @PostMapping(value="/users/password")
    public MessageDTO changePassword(HttpSession session, @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
                                 @RequestParam("repeatedNewPassword") String repeatedNewPassword)  {
        long actorId = sessManager.authorizeLogin(session);
        passwordMatches(repeatedNewPassword, newPassword);
        Validator.validatePassword(newPassword);
        return userService.changePassword(actorId, oldPassword, newPassword);
    }

    private void passwordMatches(String passwordOne, String passwordTwo) {
        if(!passwordOne.equals(passwordTwo)){
            throw new BadRequestException("Passwords do not match.");
        }
    }

    @DeleteMapping(value="/users")
    public MessageDTO deleteAccount(HttpSession session) {
        long actorId = sessManager.authorizeLogin(session);
        System.out.println("User with id " + actorId + " has logged in.");
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
    public MessageDTO unfollowUser(@PathVariable("id") long id, HttpSession session) {
        long actor = sessManager.authorizeLogin(session);
        notTheSameUser(actor, id);
        return userService.unfollowUser(actor, id);
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

}
