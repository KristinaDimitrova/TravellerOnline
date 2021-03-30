package traveller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.exception.BadRequestException;
import traveller.model.dto.MessageDTO;
import traveller.model.pojo.User;
import traveller.repository.VerificationTokenRepository;
import traveller.model.pojo.VerificationToken;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class TokenService {

    @Autowired
    private VerificationTokenRepository tokenRep;


    public VerificationToken findByToken(String token) {
        return tokenRep.findByToken(token);
    }

    public VerificationToken findByUser(User user){
        return tokenRep.findByUser(user);
    }

    public void save(VerificationToken verificationToken) {
        tokenRep.save(verificationToken);
    }

    @Transactional
    public MessageDTO confrimToken(String token) {
        VerificationToken verToken = findByToken(token);
        if(verToken == null){
            throw new BadRequestException("Invalid verification details.");
        }
        verToken.getUser().setEnabled(true);
        verToken.setConfirmedAt(LocalDateTime.now());
        return new MessageDTO("Email confirmed.");
    }

}
