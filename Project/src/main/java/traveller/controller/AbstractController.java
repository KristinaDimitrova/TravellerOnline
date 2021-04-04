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
    public MessageDTO handleException(BadRequestException e){
        log.error(e.getMessage());
        return new MessageDTO("Sorry,  " + e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDTO handleException(NotFoundException e){
        log.error(e.getMessage());
        return new MessageDTO(e.getMessage());
    }

    @ExceptionHandler(NotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public MessageDTO handleException(NotAcceptableException e){
        log.error(e.getMessage());
        return new MessageDTO("Please try again, "  + e.getMessage());
    }

    @ExceptionHandler(TechnicalIssuesException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageDTO handleException(TechnicalIssuesException e){
        log.error(e.getMessage());
        return new MessageDTO("Sorry we are experiencing technical issues. Please try again later.");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public MessageDTO handleException(AuthenticationException e){
        log.error(e.getMessage());
        return new MessageDTO(e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MessageDTO handleException(AuthorizationException e){
        log.error(e.getMessage() );
        return new MessageDTO("Unauthorized operation - " + e.getMessage());
    }

}
