package exceptions;

/**
 * Ошибка, описывающая ситуацию использования невалидного токена
 */
public class AuthException extends Exception {
    public AuthException(){
        super("Неверный ключ авторизации!");
    }

    public AuthException(String str){
        super(str);
    }
}
