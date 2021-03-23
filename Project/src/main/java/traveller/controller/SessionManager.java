package traveller.controller;

import org.springframework.stereotype.Component;
import traveller.model.POJOs.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {
    static final String LOGGED_IN = "in";

    public static boolean isUserLoggedIn(HttpSession session){
        if(session.isNew()) {
            return false;
        }
        if(session.getAttribute(LOGGED_IN) == null) {
            return false;
        }
        return true;
    }

    public static void userLogsIn(HttpSession session, long userId){
        session.setAttribute(LOGGED_IN, userId);
    }

    public static void userLogsOut(HttpSession session){
        session.setAttribute(LOGGED_IN, null);

    }
}
