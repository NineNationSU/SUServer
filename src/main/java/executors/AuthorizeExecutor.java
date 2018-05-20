package executors;

import com.google.gson.Gson;
import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.utility.CheckTokenExecutor;
import database.utility.DatabaseConnector;
import database.utility.NoteDBExecutor;
import database.utility.SQLExecutor;
import exceptions.AuthException;
import notes.Note;
import responses.ErrorResponse;
import responses.OKResponse;
import utility.MD5Utility;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static constants.Strings.AUTH_EXCEPTION;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

public class AuthorizeExecutor {
    private Map<String, String[]> request;

    public AuthorizeExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            String login = request.get("login")[0], password = request.get("password")[0];
            String sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE login='" + login +"';";
            ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
            if(set.next()){
                String passwordInDatabase = set.getString("password");
                if (!password.equals(passwordInDatabase)){
                    throw new AuthException("Неверный пароль!");
                }
                return new Gson().toJson(new Student(set));
            }else{
                throw new RegistrationExecutor.LKException();
            }
        } catch (SQLException e){
            e.printStackTrace();
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        }catch (AuthException | RegistrationExecutor.LKException e) {
            return new ErrorResponse(e.getMessage()).toString();
        } catch (Exception e){
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
