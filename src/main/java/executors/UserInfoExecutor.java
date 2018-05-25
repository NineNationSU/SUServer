package executors;

import database.objects.Student;
import database.utility.CheckTokenExecutor;
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
 * Класс, обеспечивающий взаимодействие сервера и БД и предоставляющий метод для получения данных о любом студенте
 */
public class UserInfoExecutor {
    private Map<String, String[]> request;

    public UserInfoExecutor(Map<String, String[]> request) {
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            String token = request.get(constants.Strings.TOKEN)[0];
            CheckTokenExecutor.check(token);
            Integer id = Integer.parseInt(request.get("user_id")[0]);
            String sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE id=?;";
            PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            set.next();
            String answer = new Student(set).toString();
            set.close();
            return answer;
        } catch (SQLException | NullPointerException e){
            e.printStackTrace();
            return new ServerResponse(CHECK_REQUEST_PLEASE).toString();
        }catch (AuthException e){
            return new ServerResponse(e.getMessage()).toString();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerResponse(SERVER_EXCEPTION).toString();
        }
    }
}