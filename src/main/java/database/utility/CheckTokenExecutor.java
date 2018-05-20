package database.utility;

import database.exceptions.ObjectInitException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class CheckTokenExecutor {

    public static Boolean check(String token) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String sqlRequest = "SELECT * FROM suappdatabase_test.students WHERE token='" + token +"';";
        ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        if(set.next()){
            return true;
        }
        return false;
    }
}
