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
import static constants.Strings.TOKEN;

/**
 * Класс, обеспечивающий взаимодействие сервера и БД и предоставляющий метод для получения списка одногруппников
 */
public class ClassMatesExecutor {
    private Map<String, String[]> request;

    public ClassMatesExecutor(Map<String, String[]> request) {
        this.request = request;
    }

    /**
     *
     * @return объект-студент
     */
    @Override
    public String toString() {
        try {
            String token = request.get(TOKEN)[0];
            Student student = CheckTokenExecutor.check(token);
            String sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE `group`=?;";// "' AND id!=" + student.getId() + ";";
            PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
            statement.setString(1, student.getGroupNumber());
            //statement.setInt(2, student.getId());
            ResultSet res = statement.executeQuery();
            StringBuilder sb = new StringBuilder();
            while (res.next()){
                sb.append(res.getString("last_name")).append(' ');
                sb.append(res.getString("first_name")).append(' ');
                sb.append(res.getString("phone_number")).append('\n');
            }
            return sb.toString();
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
