package traveller.controller;
import org.springframework.stereotype.Component;
import traveller.exception.AuthorizationException;
import javax.servlet.http.HttpSession;
@Component
public class SessionManager {
    static final String LOGGED_IN = "in";

    public boolean isUserLoggedIn(HttpSession session){
        if(session.isNew() || session.getAttribute(LOGGED_IN) == null) {
            return false;
        }
        return true;
    }

    public void userLogsIn(HttpSession session, long userId){
        session.setAttribute(LOGGED_IN, userId);
    }

    public void userLogsOut(HttpSession session){
        session.setAttribute(LOGGED_IN, null);
    }

    public long authorizeLogin(HttpSession session){
        if(!isUserLoggedIn(session)){
            throw new AuthorizationException("You must log in first.");
        }
        return (Long) session.getAttribute(LOGGED_IN);
    }


}
