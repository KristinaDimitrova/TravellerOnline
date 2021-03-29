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
import traveller.email.EmailSender;
import traveller.exception.*;
import traveller.model.dto.MessageDTO;
import traveller.model.dto.userDTO.EditDetailsUserDTO;
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

@Component
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRep;
    @Autowired
    private SessionManager sessManager;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private VerificationTokenService tokenService;
    @Autowired
    private EmailSender emailSender;

    @Override //method needed by Spring security
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRep.findByUsername(username);
        if (user == null) {
            throw new NotFoundException(String.format("User with username %s not found.", username));
        }
        return user;
    }

    @Transactional //Because we have more than one update statements => all or nothing
    public MessageDTO insertUser(final SignupUserDTO dto){
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
        //sending an email
        String link = "http://localhost:7878/confirm/" + token.getToken();
        emailSender.send(dto.getEmail(),
                buildEmail(dto.getFirstName(), link));
        return new MessageDTO("Registration completed.\nPlease verify your email with the link we just sent you!"); // SignUpUserResponseDTO(userRep.save(user));
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
}
