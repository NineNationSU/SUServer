package database.utility;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import constants.FileNames;
import database.exceptions.ObjectInitException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import static constants.FileNames.LOG_PASS_DATABASE;

/**
 *
 */
public  class DatabaseConnector {

    public static class CloseConnectorException extends Exception{
        CloseConnectorException(){
            super("DatabaseConnector is close");
        }
    }

    private String URL = "jdbc:mysql://localhost:3306/suappdatabase_test?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String USER;
    private String PASSWORD;
    private Connection connection;
    private Driver driver;
    private Statement statement;
    private static Boolean close = false;
    private static DatabaseConnector instance;

    public static synchronized DatabaseConnector getInstance() throws IOException, SQLException, CloseConnectorException {
        if (close){
            throw new CloseConnectorException();
        }
        if (instance == null){
            instance = new  DatabaseConnector();
            instance.build();
        }
        return instance;
    }

    private DatabaseConnector()  {
    }

    /**
     *
     * @throws SQLException если не удалось создать подключение к базе данных
     * @throws IOException если не удалось получить логин и пароль
     */
    private synchronized void build() throws SQLException, IOException {
        getLogPass();
        driver = new com.mysql.cj.jdbc.Driver();
        DriverManager.registerDriver(driver);
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
        statement = connection.createStatement();
    }

    public synchronized Statement getStatement() throws ObjectInitException, CloseConnectorException {
        if (close){
            throw new CloseConnectorException();
        }
        if (statement == null){
            throw new ObjectInitException("Объект не инициализирован!");
        }
        return statement;
    }

    public synchronized Connection getConnection() throws ObjectInitException, CloseConnectorException {
        if (close){
            throw new CloseConnectorException();
        }
        if (connection == null){
            throw new ObjectInitException("Объект не инициализирован!");
        }
        return connection;
    }

    public synchronized Driver getDriver() throws ObjectInitException, CloseConnectorException {
        if (close){
            throw new CloseConnectorException();
        }
        if (driver == null){
            throw new ObjectInitException("Объект не инициализирован!");
        }
        return driver;
    }

    public synchronized void close() throws SQLException, CloseConnectorException {
        if (close){
            throw new CloseConnectorException();
        }
        close = true;
        connection.close();
        statement.close();
    }

    /**
     * Данная функция считывает логин и пароль от базы данных из json-файла
     * @throws IOException если файл не найден или чтение не удается
     */
    private synchronized void getLogPass() throws IOException {
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
