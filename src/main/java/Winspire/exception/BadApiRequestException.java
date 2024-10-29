package Winspire.exception;

import org.apache.coyote.BadRequestException;

public class BadApiRequestException extends RuntimeException{


   public BadApiRequestException(String message){
        super(message);
    }
   public BadApiRequestException(){
        super("Bad Request !!");
    }
}
