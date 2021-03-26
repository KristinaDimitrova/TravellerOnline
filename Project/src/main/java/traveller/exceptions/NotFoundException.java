package traveller.exceptions;

import org.aspectj.weaver.ast.Not;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String reason){
        super(reason);
    }
}
