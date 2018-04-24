package database.utility;

import database.exceptions.ObjectInitException;
import database.objects.LogPass;
import database.objects.Student;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class LkDBExecutor {

    public static void insertStudentData(Student student) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String sqlRequest = "INSERT INTO suappdatabase_test.lk_ssau SET " +
                "student_id = " + student.getId() + ", " +
                "login = '" + student.getLogin() + "', " +
                "password = '" + student.getPassword() + "';";

        DatabaseConnector.getInstance().getStatement().executeUpdate(sqlRequest);
    }

    public static LogPass getStudentLKData(Integer id) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String sqlRequest = "SELECT * FROM suappdatabase_test.lk_ssau WHERE " +
                "student_id = " + id + ";";
        ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);

        if (set.next()){
            LogPass logPass = new LogPass();

            logPass.setLogin(set.getString("login"));
            logPass.setPassword(set.getString("password"));

            return logPass;
        }
        return null;
    }
}
