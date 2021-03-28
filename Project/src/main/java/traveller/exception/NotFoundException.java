package traveller.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String reason){
        super(reason);
    }
}
