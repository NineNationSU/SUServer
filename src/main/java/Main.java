
import database.utility.DatabaseConnector;
import org.apache.http.client.HttpClient;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnector connector = new DatabaseConnector();
            ResultSet res = connector.getStatement().executeQuery("SELECT * FROM suappdatabase_test.teachers");


            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
