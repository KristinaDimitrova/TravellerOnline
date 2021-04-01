package traveller.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import traveller.exception.*;
import traveller.model.dto.MessageDTO;


@Log4j2
public class AbstractController {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(BadRequestException e){
        log.error(e.getMessage());
        return "Sorry, bad request! -> " + e.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(NotFoundException e){
        log.error(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(NotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public String handleException(NotAcceptableException e){
        log.error(e.getMessage());
        return "Please try again, "  + e.getMessage();
    }

    @ExceptionHandler(TechnicalIssuesException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageDTO handleException(TechnicalIssuesException e){
        return new MessageDTO("Sorry we are experiencing technical issues. Please try again later.");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String handleException(AuthenticationException e){
        log.error(e.getMessage());

        return "Service unavailable - " + e.getMessage();
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleException(AuthorizationException e){
        log.error(e.getMessage() );
        return "Unauthorized operation - " + e.getMessage();
    }

}
