package traveller.service;

import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import traveller.exception.*;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.user.*;
import traveller.model.pojo.User;
import traveller.model.pojo.VerificationToken;
import traveller.registration.Role;
import traveller.repository.UserRepository;
import traveller.utilities.Validator;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Log4j2
@Component
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRep;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ModelMapper modelMapper;


    @Override //method needed by Spring security
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRep.findByUsername(username);
        if (user == null) {
            throw new NotFoundException(String.format("User with username %s not found.", username));
        }
        return user;
    }

    @Transactional //Because we have more than one update statements => all or nothing
    public SignUpUserResponseDTO insertUser(final SignupUserDTO dto){

        validateUsersDetails(dto);
        //encoding password
        dto.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        User user = convertSignUpUserDtoToUserEntity(dto);
        //new user must be unable to login if they have not verified their email
        user.setEnabled(false);
        userRep.save(user);
        //verification
        VerificationToken token = new VerificationToken(user);
        tokenService.save(token);
        //sending an email
        String link = "http://localhost:7878/tokens/" + token.getToken();
        Thread thread = new Thread(() -> emailService.send(dto.getEmail(),
                buildEmail(dto.getFirstName(), link), dto.getEmail()));
        thread.start();
        return convertUserEntityToSignUpResponseUserDto(user) ; // SignUpUserResponseDTO(userRep.save(user));
    }

    private void validateUsersDetails(SignupUserDTO dto){
        Validator.validateAge(dto.getAge());
        Validator.validateNames(dto.getFirstName(), dto.getLastName());
        Validator.validateEmail(dto.getEmail());
        Validator.validateUsername(dto.getUsername());
        if(!dto.getRepeatedPassword().equals(dto.getPassword())){
            throw new NotAcceptableException("passwords do not match.");
        }
        Validator.validatePassword(dto.getPassword());
        //all database-related validations
        if(userRep.findByEmail(dto.getEmail()) != null){
            throw new NotAcceptableException("account with this email already exists.");
        }
        //username taken
        if(userRep.findByUsername(dto.getUsername()) != null){
            throw new NotAcceptableException("account with this username already exists.");
        }
    }

    public List<UserWithoutPasswordDTO> getUsersByName(String firstName, String lastName) {
        List<User> list =  userRep.findByFirstNameAndLastName(firstName, lastName);
        List<UserWithoutPasswordDTO> usersWOutPass = new ArrayList<>();
        for(User u : list){
            usersWOutPass.add( new UserWithoutPasswordDTO(u));
        }
        return usersWOutPass;
    }

    public UserWithoutPasswordDTO findById(long id) {
        return new UserWithoutPasswordDTO(userRep.getById(id));
    }

    public void deleteUserByEmail(long actorId) {
        User user = userRep.getById(actorId);
        userRep.delete(user);
    }

    public void deleteUserByEmail(String email) {
        System.out.println("THIS IS THE EMAIL " + " ---> " + email);
        User user = userRep.findByEmail(email);
        userRep.delete(user);
    }

    @Transactional//could be non-transactional because the update operation is at the end
    public UserWithoutPasswordDTO changeDetails(final long actorId, final EditDetailsUserDTO reqDto) {
        User user = userRep.getById(actorId);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!encoder.matches(reqDto.getPassword(), user.getPassword())){
            throw new AuthenticationException("Wrong password.");
        }
        //check details one by one and if there are changes, incorporate them into the User user
        String emailNew = reqDto.getEmail();
        if(emailNew != user.getEmail()){
            if(userRep.findByEmail(emailNew) != null){
                throw new BadRequestException("Account with this email already exists.");
            }
            user.setEmail(reqDto.getEmail());
        }
        //if both first and last names were changed
        if(reqDto.getFirstName() != user.getFirstName() && reqDto.getLastName() != user.getLastName()){
            Validator.validateNames(reqDto.getFirstName(), reqDto.getLastName());
            user.setFirstName(reqDto.getFirstName());
            user.setLastName(reqDto.getLastName());
        }
        //if only the first name was changed
        if(reqDto.getFirstName() != user.getFirstName()){
            Validator.validateNames(reqDto.getFirstName(), user.getLastName());
        }
        //if only the last name was changed
        if(reqDto.getLastName() != user.getLastName()){
            Validator.validateNames(user.getFirstName(), reqDto.getLastName());
        }
        userRep.save(user);
        return findById(actorId);
    }

    public UserWithoutPasswordDTO verifyLogin(String username, String password) {
        User user = userRep.findByUsername(username);
        //username does not exist OR password is wrong
        if(user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new AuthenticationException("Combination of password and username is incorrect.");
        }
        //did the user verify their email?
        if(!user.isEnabled()){
            throw new AuthorizationException("You must verify your email.");
        }
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
        return new MessageDTO("User followed.");
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

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 60 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

    public User findByUsername(String username) {
        return userRep.findByUsername(username);
    }



    public SignUpUserResponseDTO convertUserEntityToSignUpResponseUserDto(User user){
        return  modelMapper.map(user, SignUpUserResponseDTO.class);
    }

    public OwnerDTO convertUserEntityToOwnerDto(User user){
        return  modelMapper.map(user,OwnerDTO.class);
    }

    public User convertSignUpUserDtoToUserEntity(SignupUserDTO signupUserDTO)   {
        User user = modelMapper.map(signupUserDTO, User.class);
        user.setCreatedAt(LocalDateTime.now());
        user.setDeleted(false);
        user.setRole(Role.USER);
        user.setEnabled(false);
        user.setPosts(new ArrayList<>());
        return user;
    }
}
