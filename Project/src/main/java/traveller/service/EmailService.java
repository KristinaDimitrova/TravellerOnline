package traveller.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;


@Service
@AllArgsConstructor
@Log4j2
public class EmailService{

    private final JavaMailSender mailSender;
    private final UserService userService;

    @Async //doesn't block the client
    public void send(String to, String htmlText, String usersEmail){
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(htmlText, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("travergy@programmer.net");
            System.out.println("About to send a confirmation email.");
            mailSender.send(mimeMessage);
            System.out.println("Email sent.");
        }catch (Exception e){
            log.error("Invalid email address. Invalid user details are being deleted...");
            userService.deleteUserByEmail(usersEmail);
        }
    }
}
