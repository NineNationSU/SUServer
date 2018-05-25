package database.exceptions;

/**
 * Исключение, характреризующее отсутствие подкючения к БД
 */
public class ObjectInitException extends Exception{
    public ObjectInitException(){
        super();
    }

    public ObjectInitException(String string){
        super(string);
    }
}
