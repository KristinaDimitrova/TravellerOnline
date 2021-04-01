package traveller.exception;

public class TechnicalIssuesException extends RuntimeException{

    public TechnicalIssuesException(String message){
        super(message);
    }

    public TechnicalIssuesException(){};
}
