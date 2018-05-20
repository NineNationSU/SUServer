package executors;

import database.objects.Student;
import database.utility.CheckTokenExecutor;
import database.utility.DatabaseConnector;
import exceptions.AuthException;
import responses.ErrorResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

public class ClassMatesExecutor {
    private Map<String, String[]> request;

    public ClassMatesExecutor(Map<String, String[]> request) {
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
            ResultSet res;
            Student student;
            if (set.next()){
                student = new Student(set);
            }else{
                return new ErrorResponse("Студент не найден").toString();
            }
            set.close();
            sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE `group`='"
                    + student.getGroup() + "';";// "' AND id!=" + student.getId() + ";";
            res =  DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
            StringBuilder sb = new StringBuilder();
            while (res.next()){
                sb.append(res.getString("last_name")).append(' ');
                sb.append(res.getString("first_name")).append(' ');
                sb.append(res.getString("middle_name")).append(' ');
                sb.append(res.getString("phone_number")).append('\n');
            }
            return sb.toString();
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
