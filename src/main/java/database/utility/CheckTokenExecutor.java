package database.utility;


import database.exceptions.ObjectInitException;
import database.objects.Student;
import exceptions.AuthException;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CheckTokenExecutor {

    /**
     *
     * @param token проверяемый токен
     * @return объект <code>Student</code>
     */
    public static Student check(String token) throws SQLException, ObjectInitException, AuthException {
        String sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE token=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setString(1, token);
        ResultSet set = statement.executeQuery();
        if(set.next()){
            Student student = new Student(set);
            set.close();
            return student;
        }
        throw new AuthException();
    }
}
