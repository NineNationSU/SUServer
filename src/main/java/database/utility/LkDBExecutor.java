package database.utility;

import database.exceptions.ObjectInitException;
import database.objects.LogPass;
import database.objects.Student;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Класс, предоставляющий метод для получения логина и пароля от ЛК
 */
public abstract class LkDBExecutor {

    public static LogPass getStudentLKData(String token) throws SQLException, ObjectInitException, NullPointerException {
        String sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE token=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setString(1, token);
        ResultSet set = statement.executeQuery();
        if (set.next()){
            LogPass logPass = new LogPass();

            logPass.setLogin(set.getString("login"));
            logPass.setPassword(set.getString("password"));

            return logPass;
        }
        throw new NullPointerException("Студент не найден!");
    }
}
