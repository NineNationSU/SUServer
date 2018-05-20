package database.utility;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.objects.StudyGroup;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class SQLExecutor {

    /**
     * <p>Данная функция добавляет студента в базу данных</p>
     * <p>А также создает таблицы в базе данных для сообщений пользователя</p>
     *
     * @param student - студент, добавляемый в БД
     * @throws IllegalObjectStateException               если не удалось привести параметры
     *                                                   объекта "студент" к SQL-запросу
     * @throws SQLException                              если не удалось выполнить SQL запрос
     * @throws IOException                               если не удалось создать подключение к БД
     * @throws ObjectInitException                       если не удалось создать подключение к БД
     * @throws DatabaseConnector.CloseConnectorException если подключение к БД закрыто
     */
    public synchronized static void insertNewStudent(Student student) throws IllegalObjectStateException, SQLException, IOException, ObjectInitException, DatabaseConnector.CloseConnectorException, NoSuchAlgorithmException {
        String params = student.toSQLInsertString();


        String sqlRequest = "INSERT INTO suappdatabase_test.students SET " + params + ";";
        DatabaseConnector.getInstance().getStatement().executeUpdate(sqlRequest);

        sqlRequest = "SELECT MAX(`id`) FROM suappdatabase_test.students;";
        ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        set.next();
        Integer id = set.getInt(1);
        set.close();

        sqlRequest = "CREATE TABLE IF NOT EXISTS ml_id" + id + " LIKE suappdatabase_test.message_list_template";
        DatabaseConnector.getInstance().getStatement().execute(sqlRequest);
    }

    public synchronized static ResultSet getStudentsByGroup(String groupNumber) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String sqlRequest = "SELECT suappdatabase_test.students.id from suappdatabase_test.students "
                + "WHERE `group` = " + '\'' + groupNumber + '\'' + ';';
        // TODO
        System.out.println(sqlRequest);
        return DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
    }

    public synchronized static StudyGroup getGroupByStudentId(Integer studentId) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String sqlRequest = "SELECT suappdatabase_test.students.`group` from suappdatabase_test.students "
                + "WHERE id = " + studentId + ';';
        ResultSet res = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        res.next();
        return new StudyGroup().setNumber(res.getString("group"));
    }


}
