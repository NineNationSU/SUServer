package executors;

import com.google.gson.Gson;
import database.exceptions.IllegalObjectStateException;
import database.objects.Student;
import database.objects.StudyGroup;
import database.utility.CheckTokenExecutor;
import database.utility.DatabaseConnector;
import database.utility.SQLExecutor;
import exceptions.AuthException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import responses.ErrorResponse;
import ssau.lk.Grabber;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import static constants.FileNames.TIMETABLE_FOLDER;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

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
            String token = request.get("token")[0];
            if (!CheckTokenExecutor.check(token)) {
                throw new AuthException();
            }
            String sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE token='" + token + "';";
            System.out.println(sqlRequest);
            ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
            Student student;
            if (set.next()){
                student = new Student(set);
            }else{
                return new ErrorResponse("Студент не найден").toString();
            }
            set.close();

            String lk = Grabber.getLK(student.getLogin(), student.getPassword());

            return salary(lk);
        }catch (NullPointerException e){
            e.printStackTrace();
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        }catch (AuthException e){
            return new ErrorResponse(e.getMessage()).toString();
        }catch (Exception e){
            e.printStackTrace();
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
