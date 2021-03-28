package traveller.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import traveller.model.pojo.User;
import traveller.model.repository.VerificationTokenRepository;
import traveller.utilities.VerificationToken;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository verTokRep;


    public VerificationToken findByToken(String token) {
        return verTokRep.findByToken(token);
    }

    public VerificationToken findByUser(User user){
        return verTokRep.findByUser(user);
    }
    public void save(User user, String token) {
        VerificationToken verToken = new VerificationToken(user, token);
        //set expiry date to 2 h
        verToken.setExpriryDate(calculateExpiryDate(120));
    }

    //calculate expiry date
    private Timestamp calculateExpiryDate(int expiryTimeInMinutes){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(calendar.getTime().getTime());
        
    }
}
