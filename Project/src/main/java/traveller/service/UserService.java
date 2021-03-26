package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import traveller.exceptions.InvalidRegistrationInputException;
import traveller.exceptions.NotFoundException;
import traveller.model.DTO.EditDetailsUserDTO;
import traveller.model.DTO.SignupResponseUserDTO;
import traveller.model.DTO.SignupUserDTO;
import traveller.model.DTO.UserWithoutPasswordDTO;
import traveller.model.POJOs.User;
import traveller.model.repositoriesUser.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository userRep;

    public SignupResponseUserDTO insertUser(SignupUserDTO requestDTO){
        //all database-related validations
        //email doesn't exist
        if(userRep.findByEmail(requestDTO.getEmail()) != null){
            throw new InvalidRegistrationInputException("Account with this email already exists.");
        }
        //username doesn't exist
        if(userRep.findByUsername(requestDTO.getUsername()) != null){
            throw new InvalidRegistrationInputException("Account with this username already exists.");
        }
        //encoding password
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        requestDTO.setPassword(encoder.encode(requestDTO.getPassword()));
        User user = new User(requestDTO);
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

    public UserWithoutPasswordDTO changeDetails(long actorId, EditDetailsUserDTO dto) {
        //check details one by one
        User user = userRep.getById(actorId);
        if(!user.getFirstName().equals(dto.getFirstName())){

        }
        if(!user.getLastName().equals(dto.getLastName())){
            //change in db
        }
        if(!user.getEmail().equals(dto.getEmail())){
            //validate new email
            //change in db
        }
        return findById(actorId);
    }
}
