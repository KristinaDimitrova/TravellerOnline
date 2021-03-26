package traveller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import traveller.exceptions.BadRequestException;
import traveller.exceptions.InvalidRegistrationInputException;
import traveller.exceptions.LoginException;
import traveller.exceptions.AuthorizationException;

public class AbstractController {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(BadRequestException e){
        return "Sorry, bad request! -> " + e.getMessage();
    }

    @ExceptionHandler(InvalidRegistrationInputException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String handleException(InvalidRegistrationInputException e){
        return "We couldn't create an account for you. "  + e.getMessage();
    }

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleException(LoginException e){
        return "Forbidden operation - " + e.getMessage();
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleException(AuthorizationException e){
        return "Unathorized operation - " + e.getMessage();
    }
}
