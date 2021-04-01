package traveller.email;

import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import traveller.exception.BadRequestException;


@Service
@AllArgsConstructor
@EnableAsync //is this redundant? todo research
public class EmailService implements EmailSender{


    private final JavaMailSender mailSender;
    private final static Logger LOGGER =  //using it to make logs when sending an email
            LoggerFactory.getLogger(EmailService.class);
    @Override
    @Async //doesn't block the client
    public void send(String to, String email) {
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("travergy@programmer.net");
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            LOGGER.error("failed to send email", e);
            throw new BadRequestException("invalid email address");
        }
    }
}
