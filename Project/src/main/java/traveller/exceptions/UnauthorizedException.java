package traveller.exceptions;

public class UnauthorizedException extends Throwable {
    public UnauthorizedException(String reason){
        super(reason);
    }
}
