package database.utility;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import constants.FileNames;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import static constants.FileNames.LOG_PASS_DATABASE;

/**
 *
 */
public class DatabaseConnector {
    private String URL = "jdbc:mysql://localhost:3306/suappdatabase_test?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String USER;
    private String PASSWORD;
    private Connection connection;
    private Driver driver;
    private Statement statement;

    /**
     *
     * @throws SQLException если не удалось создать подключение к базе данных
     * @throws IOException если не удалось получить логин и пароль
     */
    public DatabaseConnector() throws SQLException, IOException {
        getLogPass();
        System.out.println(USER);
        System.out.println(PASSWORD);
        driver = new com.mysql.cj.jdbc.Driver();
        DriverManager.registerDriver(driver);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        statement = connection.createStatement();
    }

    public Statement getStatement() {
        return statement;
    }

    public Connection getConnection() {
        return connection;
    }

    public Driver getDriver() {
        return driver;
    }

    public void close() throws SQLException {
        connection.close();
    }

    /**
     * Данная функция считывает логин и пароль от базы данных из json-файла
     * @throws IOException если файл не найден или чтение не удается
     */
    private void getLogPass() throws IOException {
        JsonReader reader =  new Gson().newJsonReader(new FileReader(LOG_PASS_DATABASE));
        reader.beginObject();
        reader.nextName();
        USER = reader.nextString();
        reader.nextName();
        PASSWORD = reader.nextString();
        reader.endObject();
        reader.close();
    }
}
