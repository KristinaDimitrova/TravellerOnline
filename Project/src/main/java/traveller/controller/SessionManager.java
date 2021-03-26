package traveller.controller;

import org.springframework.stereotype.Component;
import traveller.exceptions.AuthorizationException;
import traveller.model.POJOs.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
@Component
public class SessionManager {
    static final String LOGGED_IN = "in";

    public boolean isUserLoggedIn(HttpSession session){
        if(session.isNew()) {
            return false;
        }
        if(session.getAttribute(LOGGED_IN) == null) {
            return false;
        }
        return true;
    }

    public void userLogsIn(HttpSession session, long userId){
        session.setAttribute(LOGGED_IN, userId);
    }

    public void userLogsOut(HttpSession session){
        session.invalidate();
    }

    public long userHasLoggedIn(HttpSession session){
        if(!isUserLoggedIn(session)){
            throw new AuthorizationException("You must log in first.");
        }
        return (Long) session.getAttribute(LOGGED_IN);
    }
}
