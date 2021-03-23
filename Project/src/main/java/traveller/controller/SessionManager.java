package traveller.controller;

import traveller.model.POJOs.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionManager {
    private static final String LOGGED_IN = "in";

    public static boolean validateLogin(HttpSession session){
        if(session.isNew()) {
            return false;
        }
        if(session.getAttribute(LOGGED_IN) == null) {
            return false;
        }
        return true;
    }

    public static void userLogsIn(HttpSession session, User user){
        session.setAttribute(LOGGED_IN, user);
    }

    public static void userLogsOut(HttpSession session, User user){
        session.setAttribute(LOGGED_IN, null);
    }
}
