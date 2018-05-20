package executors;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.utility.CheckTokenExecutor;
import database.utility.DatabaseConnector;
import database.utility.LkDBExecutor;
import exceptions.AuthException;
import org.apache.commons.lang3.ObjectUtils;
import responses.ErrorResponse;
import responses.OKResponse;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static constants.Strings.AUTH_EXCEPTION;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

public class UserInfoExecutor {
    private Map<String, String[]> request;

    public UserInfoExecutor(Map<String, String[]> request) {
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            String token = request.get("token")[0];
            if (!CheckTokenExecutor.check(token)) {
                throw new AuthException();
            }
            Integer id = Integer.parseInt(request.get("user_id")[0]);
            String sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE id=" + id + ";";
            ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
            if (set.next()){
                return new Student(set).toString();
            }else{
                throw new Exception("Студент не найден");
            }
        } catch (SQLException | NullPointerException e){
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