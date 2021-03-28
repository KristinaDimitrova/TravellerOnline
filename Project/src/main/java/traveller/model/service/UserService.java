package traveller.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import traveller.controller.SessionManager;
import traveller.exception.AuthenticationException;
import traveller.exception.BadRequestException;
import traveller.exception.InvalidRegistrationInputException;
import traveller.exception.NotFoundException;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.userDTO.EditDetailsUserDTO;
import traveller.model.dto.userDTO.SignUpUserResponseDTO;
import traveller.model.dto.userDTO.SignupUserDTO;
import traveller.model.dto.userDTO.UserWithoutPasswordDTO;
import traveller.model.pojo.User;
import traveller.model.repository.UserRepository;
import traveller.utilities.Validate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class UserService implements UserDetailsService { //TODO try it without this implementation
    @Autowired
    private UserRepository userRep;
    @Autowired
    private SessionManager sessManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRep.findByUsername(username);
        if (user == null) {
            throw new NotFoundException(String.format("User with username %s not found.", username));
        }
        return user;
    }

    public SignUpUserResponseDTO insertUser(SignupUserDTO dto){
        System.out.println(dto.getFirstName());
        System.out.println(dto.getLastName());
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
        //enable the new user
        user.setEnabled(false);
        //todo send email with a thread
        return new SignUpUserResponseDTO(userRep.save(user));
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
        return new UserWithoutPasswordDTO(userRep.getById(id));
    }

    public void deleteUser(long actorId) {
        userRep.deleteUserById(actorId);
    }

    public UserWithoutPasswordDTO changeDetails(final long actorId, final EditDetailsUserDTO reqDto) {
        User user = userRep.getById(actorId);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(reqDto.getPassword(), user.getPassword())){
            throw new AuthenticationException("Wrong password.");
        }
        //check details one by one and if there are changes, incorporate them into the User user
        if(reqDto.getEmail() != user.getEmail()){
            user.setEmail(reqDto.getEmail());
        }
        //if both first and last names were changed
        if(reqDto.getFirstName() != user.getFirstName() && reqDto.getLastName() != user.getLastName()){
            Validate.firstLastNames(reqDto.getFirstName(), reqDto.getLastName());
            user.setFirstName(reqDto.getFirstName());
            user.setLastName(reqDto.getLastName());
        }
        //if only the first name was changed
        if(reqDto.getFirstName() != user.getFirstName()){
            Validate.firstLastNames(reqDto.getFirstName(), user.getLastName());
        }
        //if only the last name was changed
        if(reqDto.getLastName() != user.getLastName()){
            Validate.firstLastNames(user.getFirstName(), reqDto.getLastName());
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
