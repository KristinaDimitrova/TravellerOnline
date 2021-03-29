package traveller.email;

import org.springframework.stereotype.Component;

@Component
public interface EmailSender {
    void send(String to, String email);
}
