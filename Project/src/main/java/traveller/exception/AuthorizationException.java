package traveller.exception;

public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String reason){
        super(reason);
    }
}
