package traveller.email;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import traveller.exception.TechnicalIssuesException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@AllArgsConstructor
@Log4j2
public class EmailService implements EmailSender{

    private final JavaMailSender mailSender;

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
            log.error("Confirmation email not sent.", e);
            throw new TechnicalIssuesException("Confirmation email could not be sent.");
        }
    }
}
