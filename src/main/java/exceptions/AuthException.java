package exceptions;

public class AuthException extends Exception {
    public AuthException(){
        super();
    }

    public AuthException(String str){
        super(str);
    }
}
