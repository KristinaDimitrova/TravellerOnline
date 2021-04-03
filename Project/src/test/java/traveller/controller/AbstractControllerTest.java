package traveller.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import traveller.exception.AuthenticationException;
import traveller.exception.AuthorizationException;
import traveller.exception.BadRequestException;
import traveller.exception.NotAcceptableException;
import traveller.exception.NotFoundException;
import traveller.exception.TechnicalIssuesException;

public class AbstractControllerTest {

    private AbstractController abstractController = new AbstractController();

    @Test
    public void testHandleAuthenticationException() {
        assertEquals("Authentication required",
                abstractController.handleException(new AuthenticationException("Authentication required")).getText());
    }

    @Test
    public void testHandleNotFoundException() {
        assertEquals("",
                abstractController.handleException(new NotFoundException("")).getText());
    }

    @Test
    public void testHandleTechnicalIssuesException() {
        assertEquals("Sorry we are experiencing technical issues. Please try again later.",
                abstractController.handleException(new TechnicalIssuesException()).getText());
    }

    @Test
    public void testHandleAuthorizationException() {
        assertEquals("Unauthorized operation - ",
                abstractController.
                        handleException(new AuthorizationException("")).getText());
    }

    @Test
    public void testHandleBadRequestException() {
        assertEquals("Sorry,  bro",
                abstractController.handleException(new BadRequestException("bro")).getText());
    }

    @Test
    public void testHandleNotAcceptableException() {
        assertEquals("Please try again, ",
                abstractController.handleException(new NotAcceptableException("")).getText());
    }

}

