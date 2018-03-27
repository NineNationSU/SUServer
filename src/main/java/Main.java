
import database.exceptions.IllegalObjectStateException;
import database.objects.Message;
import database.objects.Student;
import database.utility.DatabaseConnector;
import database.utility.SQLExecutor;
import executors.MessageGetExecutor;
import executors.MessageSendExecutor;
import executors.RegistrationExecutor;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        //Message message = new Message().setTime(new Timestamp(new Date().getTime()));
        //System.out.println(message);
        get("/registration", (req, res) -> {
            return new RegistrationExecutor(req.queryMap().toMap()).toString();
        });
        get("/sendMessage", (req, res) -> {
            return new MessageSendExecutor(req.queryMap().toMap()).toString();
        });
        get("/getMessages", (req, res) -> {
            return new MessageGetExecutor(req.queryMap().toMap()).toString();
        });
        get("/hello", (req, res) -> {
            Map<String, String[]> map = req.queryMap().toMap();
            System.out.println(Arrays.toString(map.get("p")));
            System.out.println(map.get("p")[0]);

            return 0;
        });
        /*try {
            DatabaseConnector connector = DatabaseConnector.getInstance();

            Student student = new Student()
                    .setLogin("test_login2")
                    .setPassword("test_password")
                    .setFirstName("Иван")
                    .setMiddleName("Иванович")
                    .setLastName("Иванов")
                    .setBirthday("01.01.2000")
                    .setGender("муж")
                    .setPhoneNumber("+79991234567")
                    .setGroup("6209-010302D")
                    .setGroupManager(true);

            System.out.println(student);
            //SQLExecutor.insertNewStudent(student);
            connector.close();

            String str = "{\"first_name\":\"Петр\",\"middle_name\":\"Петрович\",\"last_name\":\"Петров\",\"birthday\":\"01.01.2000\",\"gender\":\"муж\",\"phone_number\":\"+79991234567\",\"group\":\"6209-010302D\",\"group_manager\":1}\n";

            Student st = Student.loadFromJson(str);

            System.out.println(st);

        } catch (SQLException | IOException | DatabaseConnector.CloseConnectorException e) {
            e.printStackTrace();
        }*/
    }
}
