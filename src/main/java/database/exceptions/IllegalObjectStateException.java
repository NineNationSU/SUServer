package database.exceptions;

/**
 * Исключение, характеризующее некорректное состояние объекта
 */
public class IllegalObjectStateException extends Exception{
    public IllegalObjectStateException(String s){
        super(s);
    }
}
