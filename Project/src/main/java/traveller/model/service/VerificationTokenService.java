package traveller.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.model.pojo.User;
import traveller.model.repository.VerificationTokenRepository;
import traveller.model.pojo.VerificationToken;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class VerificationTokenService {

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

}
