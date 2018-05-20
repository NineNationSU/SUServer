package exceptions;

public class AuthException extends Exception {
    public AuthException(){
        super("Неверный ключ авторизации!");
    }

    public AuthException(String str){
        super(str);
    }
}
