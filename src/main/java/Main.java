
import database.exceptions.IllegalObjectStateException;
import database.objects.Message;
import database.objects.Student;
import database.objects.StudyGroup;
import database.utility.DatabaseConnector;
import database.utility.SQLExecutor;
import executors.*;
import utility.http.RequestUtility;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/registration", (req, res) -> new RegistrationExecutor(req.queryMap().toMap()).toString());
        get("/sendMessage", (req, res) -> new MessageSendExecutor(req.queryMap().toMap()).toString());
        get("/getMessages", (req, res) -> new MessageGetExecutor(req.queryMap().toMap()).toString());
        get("/addNote", (req, res) -> new AddNoteExecutor(req.queryMap().toMap()).toString());
        get("/getNote", (req, res) -> new GetNoteExecutor(req.queryMap().toMap()).toString());
        get("/addLK", (req, res) -> new AddLKExecutor(req.queryMap().toMap()).toString());
        get("/getTimeTable", (req, res) -> new GetTimeTableExecutor(req.queryMap().toMap()).toString());
    }
}
