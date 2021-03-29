package traveller.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import traveller.controller.SessionManager;
import traveller.exception.*;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.userDTO.EditDetailsUserDTO;
import traveller.model.dto.userDTO.SignUpUserResponseDTO;
import traveller.model.dto.userDTO.SignupUserDTO;
import traveller.model.dto.userDTO.UserWithoutPasswordDTO;
import traveller.model.pojo.User;
import traveller.model.pojo.VerificationToken;
import traveller.model.repository.UserRepository;
import traveller.utilities.Validate;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Service
public class UserService implements UserDetailsService { //TODO try it without this implementation
    @Autowired
    private UserRepository userRep;
    @Autowired
    private SessionManager sessManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private VerificationTokenService tokenService;

    @Override //this is needed by Spring security
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRep.findByUsername(username);
        if (user == null) {
            throw new NotFoundException(String.format("User with username %s not found.", username));
        }
        return user;
    }

    @Transactional //Because we have more than one update statements -> all or nothing
    public String insertUser(SignupUserDTO dto){
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
        //username taken
        if(userRep.findByUsername(dto.getUsername()) != null){
            throw new InvalidRegistrationInputException("Account with this username already exists.");
        }
        //encoding password
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        User user = new User(dto);
        //new user must be unable to login if they have not verified their email
        user.setEnabled(false);
        userRep.save(user);
        //verification
        VerificationToken token = new VerificationToken(user);
        tokenService.save(token);
        //todo send email
        return token.getToken(); // SignUpUserResponseDTO(userRep.save(user));
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
        User user = userRep.findByUsername(username);

        //check if password is the same
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        //username does not exist OR password is wrong
        if(user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new AuthenticationException("Combination of password and username is incorrect.");
        }
        //did the user verify their email?
        if(!user.isEnabled()){
            throw new AuthorizationException("You must verify your email.");
        }
        sessManager.userLogsIn(session, user.getId());
        return new UserWithoutPasswordDTO(user);
    }

    public MessageDTO changePassword(long userId, String oldPassword, String newPassword) {
        User user = userRep.getById(userId);
        if(!bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
            throw new AuthenticationException("Old password is incorrect.");
        }
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRep.save(user);
        return new MessageDTO("Password changed.");
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

    @Transactional
    public MessageDTO confrimToken(String token) {
        VerificationToken verToken = tokenService.findByToken(token);
        if(verToken == null){
            throw new BadRequestException("Invalid verification details.");
        }
        verToken.getUser().setEnabled(true);
        verToken.setConfirmedAt(LocalDateTime.now());
        return new MessageDTO("Email confirmed.");
    }
}
