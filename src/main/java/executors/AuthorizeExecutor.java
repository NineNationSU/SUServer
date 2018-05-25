package executors;

import com.google.gson.Gson;
import database.objects.Student;
import database.utility.DatabaseConnector;
import exceptions.AuthException;
import responses.ServerResponse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

/**
 * Класс, обеспечивающий взаимодействие сервера и БД и предоставляющий метод для авторизации в клиентских приложениях
 */
public class AuthorizeExecutor {
    private Map<String, String[]> request;

    public AuthorizeExecutor(Map<String, String[]> request){
        this.request = request;
    }

    /**
     *
     * @return объект-студент
     */
    @Override
    public String toString() {
        try {
            String login = request.get("login")[0], password = request.get("password")[0];
            String sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE login=?;";
            PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
            statement.setString(1, login);
            ResultSet set = statement.executeQuery();
            if(set.next()){
                String passwordInDatabase = set.getString("password");
                if (!password.equals(passwordInDatabase)){
                    set.close();
                    throw new AuthException("Неверный пароль!");
                }
                Student student = new Student(set);
                set.close();
                return new Gson().toJson(student);
            }else{
                throw new RegistrationExecutor.LKException();
            }
        } catch (SQLException e){
            e.printStackTrace();
            return new ServerResponse(CHECK_REQUEST_PLEASE).toString();
        }catch (AuthException | RegistrationExecutor.LKException e) {
            return new ServerResponse(e.getMessage()).toString();
        } catch (Exception e){
            return new ServerResponse(SERVER_EXCEPTION).toString();
        }
    }
}
