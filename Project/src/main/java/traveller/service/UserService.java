package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import traveller.controller.SessionManager;
import traveller.exceptions.AuthenticationException;
import traveller.exceptions.InvalidRegistrationInputException;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.EditDetailsUserDTO;
import traveller.model.DTO.SignupResponseUserDTO;
import traveller.model.DTO.SignupUserDTO;
import traveller.model.DTO.UserWithoutPasswordDTO;
import traveller.model.POJOs.User;
import traveller.model.repositoriesUser.UserRepository;
import traveller.utilities.Validate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRep;

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
        //check details one by one
        userRep.save(requestDTO);
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
        SessionManager.userLogsIn(session, user.getId());
        return new UserWithoutPasswordDTO(user);
    }
}
