package traveller.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import traveller.controller.SessionManager;
import traveller.exceptions.AuthenticationException;
import traveller.exceptions.BadRequestException;
import traveller.exceptions.InvalidRegistrationInputException;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.*;
import traveller.model.DTO.userDTO.EditDetailsUserDTO;
import traveller.model.DTO.userDTO.SignupResponseUserDTO;
import traveller.model.DTO.userDTO.SignupUserDTO;
import traveller.model.DTO.userDTO.UserWithoutPasswordDTO;
import traveller.model.POJOs.User;
import traveller.model.repositories.UserRepository;
import traveller.utilities.Validate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRep;
    @Autowired
    private SessionManager sessManager;

    public SignupResponseUserDTO insertUser(SignupUserDTO dto){
        Validate.firstLastNames(dto.getFirstName(), dto.getLastName());
        Validate.email(dto.getEmail());
        Validate.username(dto.getUsername());
        if(!dto.getRepeatedPassword().equals(dto.getPassword())){
            throw new InvalidRegistrationInputException("Passwords do not match.");
        }
        Validate.password(dto.getPassword());
        //all database-related validations
        if(userRep.findByEmail(dto.getEmail()) != null){
            throw new InvalidRegistrationInputException("Account with this email already exists.");
        }
        //username doesn't exist
        if(userRep.findByUsername(dto.getUsername()) != null){
            throw new InvalidRegistrationInputException("Account with this username already exists.");
        }
        //encoding password
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        dto.setPassword(encoder.encode(dto.getPassword()));
        User user = new User(dto);
        SignupResponseUserDTO responseDTO = new SignupResponseUserDTO(user);
        //todo send email with a thread
        return responseDTO;
    }

    public List<UserWithoutPasswordDTO> getUsersByName(String firstName, String lastName) {
        List<User> list =  userRep.findByFirstNameAndLastName(firstName, lastName);
        List<UserWithoutPasswordDTO> usersWOutPass = new ArrayList<>();
        for(User u : list){
            usersWOutPass.add(new UserWithoutPasswordDTO(u));
        }
        return usersWOutPass;
    }

    public UserWithoutPasswordDTO findById(long id) {
        Optional<User> user = userRep.findById(id);
        if(user.isPresent()){
            return new UserWithoutPasswordDTO(user.get());
        }
        throw new NotFoundException("User not found");
    }

    public void deleteUser(long actorId) {
        userRep.deleteUserById(actorId);
    }

    public UserWithoutPasswordDTO changeDetails(long actorId, EditDetailsUserDTO requestDTO) {
        User user = userRep.getById(actorId);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(requestDTO.getPassword(), user.getPassword())){
            throw new AuthenticationException("Wrong password.");
        }
        //check details one by one and if there are changes, incorporate them into the User user
        if(requestDTO.getEmail() != user.getEmail()){
            user.setEmail(requestDTO.getEmail());
        }
        if(requestDTO.getFirstName() != user.getFirstName() && requestDTO.getLastName() != user.getLastName()){
            Validate.firstLastNames(requestDTO.getFirstName(), requestDTO.getLastName());
            user.setFirstName(requestDTO.getFirstName());
            user.setLastName(requestDTO.getLastName());
        }
        userRep.save(user); //todo save(user) -> user that as fields old and new
        return findById(actorId);
    }

    public UserWithoutPasswordDTO loginWtUsername(String username, String password, HttpSession session) {
        //check if password is the same
        User user = userRep.findByUsername(username);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        //username does not exist OR password is wrong
        if(user == null || !encoder.matches(password, user.getPassword())){
            throw new AuthenticationException("Combination of password and username is incorrect.");
        }
        sessManager.userLogsIn(session, user.getId());
        return new UserWithoutPasswordDTO(user);
    }

    public UserWithoutPasswordDTO changePassword(long userId, String oldPassword, String newPassword) {
        User user = userRep.getById(userId);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(oldPassword, user.getPassword())){
            throw new AuthenticationException("Old password is incorrect.");
        }
        user.setPassword(newPassword);
        return new UserWithoutPasswordDTO(userRep.save(user));
    }

    public MessageDTO followUser(long follower, long followed){
        User followedUser = userRep.getById(followed); //followed user exists
        User actingUser = userRep.getById(follower);
        if(actingUser.getFollowedUsers().contains(followedUser)){
            throw new BadRequestException("You have already followed this user.");
        }
        actingUser.getFollowedUsers().add(followedUser);
        userRep.save(actingUser);
        return new MessageDTO("Followed.");
    }

    public MessageDTO unfollowUser(long follower, long followed){
        User followedUser = userRep.getById(followed); //does followed user exist ?
        User actingUser = userRep.getById(follower);
        if(!actingUser.getFollowedUsers().contains(followedUser)){
            throw new BadRequestException("You are not following this user.");
        }
        actingUser.getFollowedUsers().remove(followedUser);
        userRep.save(actingUser);
        return new MessageDTO("Unfollowed");
    }

}