package database.utility;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.objects.StudyGroup;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;


public abstract class SQLExecutor {

    /**
     * <p>Данная функция добавляет студента в базу данных</p>
     * <p>А также создает таблицы в базе данных для сообщений пользователя</p>
     *
     * @param student - студент, добавляемый в БД
     * @throws IllegalObjectStateException               если не удалось привести параметры
     *                                                   объекта "студент" к SQL-запросу
     * @throws SQLException                              если не удалось выполнить SQL запрос
     * @throws ObjectInitException                       если не удалось создать подключение к БД
     * @throws NoSuchAlgorithmException                  при ошибке получения токена
     */
    public synchronized static void insertNewStudent(Student student) throws IllegalObjectStateException, SQLException, ObjectInitException {
        student.sqlCheck();
        String sqlRequest = "INSERT INTO suappdatabase_test.students SET " +
                "login=?, token=?, password=?, first_name=?, middle_name=?, " +
                "last_name=?, birthday=?, gender=?, `group`=?, " +
                "group_proforg=?, group_president=?, group_manager=?," +
                "phone_number=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setString(1, student.getLogin());
        statement.setString(2, student.getToken());
        statement.setString(3, escapeJava(student.getPassword()));
        statement.setString(4, student.getFirstName());
        statement.setString(5, student.getMiddleName());
        statement.setString(6, student.getLastName());
        statement.setDate(7, Date.valueOf(student.getBirthday()));
        statement.setString(8, student.getGender());
        statement.setString(9, student.getGroupNumber());
        statement.setInt(10, student.getGroupProforg());
        statement.setInt(11, student.getGroupPresident());
        statement.setInt(12, student.getGroupManager());
        if (student.getPhoneNumber()!= null) {
            statement.setString(13, escapeJava(student.getPhoneNumber()));
        }else{
            statement.setString(13, "Не указан");
        }
        statement.executeUpdate();

        sqlRequest = "SELECT MAX(`id`) FROM suappdatabase_test.students;";
        ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        set.next();
        Integer id = set.getInt(1);
        set.close();

        sqlRequest = "CREATE TABLE IF NOT EXISTS ml_id" + id + " LIKE suappdatabase_test.message_list_template";
        DatabaseConnector.getInstance().getStatement().execute(sqlRequest);
    }

    public synchronized static ResultSet getStudentsByGroup(String groupNumber) throws SQLException, ObjectInitException {
        String sqlRequest = "SELECT suappdatabase_test.students.id from suappdatabase_test.students WHERE `group`=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setString(1, groupNumber);
        return statement.executeQuery();
    }

    public synchronized static StudyGroup getGroupByStudentId(Integer studentId) throws SQLException, ObjectInitException {
        String sqlRequest = "SELECT suappdatabase_test.students.`group` from suappdatabase_test.students WHERE id=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setInt(1, studentId);
        ResultSet res = statement.executeQuery();
        res.next();
        return new StudyGroup().setNumber(res.getString("group"));
    }


}
