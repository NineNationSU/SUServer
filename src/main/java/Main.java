
import database.exceptions.IllegalObjectStateException;
import database.objects.Message;
import database.objects.Student;
import database.objects.StudyGroup;
import database.utility.DatabaseConnector;
import database.utility.SQLExecutor;
import executors.*;
import ssau.lk.Grabber;
import ssau.lk.StudentInfoGrabber;
import utility.http.RequestUtility;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/registration", (req, res) ->{
            System.out.println(res);
            String s = new RegistrationExecutor(req.queryMap().toMap()).toString();
            System.out.println(s);
            return s;
        });
        get("/sendMessage", (req, res) ->{
            System.out.println(res);
            return  new MessageSendExecutor(req.queryMap().toMap()).toString();
        });
        get("/getMessages", (req, res) ->{
            System.out.println(res);
            return  new MessageGetExecutor(req.queryMap().toMap()).toString();
        });
        get("/authorize", (req, res) ->{
            System.out.println(res);
            String str = new AuthorizeExecutor(req.queryMap().toMap()).toString();
            System.out.println(str);
            return str;
        });
        get("/userInfo", (req, res) ->{
            System.out.println(res);
            String str = new UserInfoExecutor(req.queryMap().toMap()).toString();
            System.out.println(str);
            return str;
        });
        get("/addNote", (req, res) ->{
            System.out.println(res);
            return  new AddNoteExecutor(req.queryMap().toMap()).toString();
        });
        get("/getNote", (req, res) ->{
            System.out.println(res);
            return  new GetNoteExecutor(req.queryMap().toMap()).toString();
        });
        //get("/addLK", (req, res) -> new AddLKExecutor(req.queryMap().toMap()).toString()});
        get("/getTimeTable", (req, res) ->{
            System.out.println(res);
            return  new GetTimeTableExecutor(req.queryMap().toMap()).toString();
        });
    }
}
