package traveller.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import traveller.exception.*;
import traveller.model.dto.MessageDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class AbstractController {

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDTO handleException(HttpServerErrorException.InternalServerError e){
        log.error(e.getMessage());
        return new MessageDTO("We are sorry,  " + e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDTO handleException(BadRequestException e){
        log.error(e.getMessage());
        return new MessageDTO("We are sorry,  " + e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MessageDTO handleException(
            MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new MessageDTO("There seems to be a problem with the input.");
    }

}
