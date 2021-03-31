package traveller.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import traveller.exception.*;
import traveller.model.dto.MessageDTO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDTO handleException(BadRequestException e){
        return new MessageDTO("Oops, " + e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageDTO handleException(NotFoundException e){
        return new MessageDTO(e.getMessage());
    }

    @ExceptionHandler(NotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE) //fixme
    public MessageDTO handleException(NotAcceptableException e){
        return new MessageDTO("Please try again, "  + e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageDTO handleException(SQLException e){
        return new MessageDTO("Database maintenance. Please try again later");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public MessageDTO handleException(AuthenticationException e){
        return new MessageDTO("Service unavailable - " + e.getMessage());
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public MessageDTO handleException(AuthorizationException e){
        return new MessageDTO("Unauthorized operation - " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleException(MethodArgumentNotValidException e){
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


/*    @Scheduled(fixedDelay = 1000) //every minute
    public void ScheduledLogs(){
        use logs
    }*/
}
