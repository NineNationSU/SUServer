package executors;

import database.objects.Student;
import database.utility.CheckTokenExecutor;
import exceptions.AuthException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import responses.ServerResponse;
import ssau.lk.Grabber;

import java.util.Map;

import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

/**
 * Класс, обеспечивающий взаимодействие сервера и БД и предоставляющий метод для  получения суммы стипендии
 */
public class SalaryExecutor {
    public static String salary(String lk){
        Document document = Jsoup.parse(lk);
        return document.select("div.right_col_inside").text();
    }

    private Map<String, String[]> request;

    public SalaryExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            String token = request.get(constants.Strings.TOKEN)[0];
            Student student = CheckTokenExecutor.check(token);
            String lk = Grabber.getLK(student.getLogin(), student.getPassword());
            return salary(lk);
        }catch (NullPointerException e){
            return new ServerResponse(CHECK_REQUEST_PLEASE).toString();
        }catch (AuthException e){
            return new ServerResponse(e.getMessage()).toString();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerResponse(SERVER_EXCEPTION).toString();
        }
    }
}
