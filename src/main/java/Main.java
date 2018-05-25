
import executors.*;

import java.net.URLDecoder;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/registration", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String s = new RegistrationExecutor(req.queryMap().toMap()).toString();
            System.out.println(s);
            return s;
        });
        get("/sendMessage", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new MessageSendExecutor(req.queryMap().toMap()).toString();
			System.out.println(str);
            return str;
        });
        get("/getMessages", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new MessageGetExecutor(req.queryMap().toMap()).toString();
			System.out.println(str);
            return str;
        });
        get("/authorize", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new AuthorizeExecutor(req.queryMap().toMap()).toString();
            System.out.println(str);
            return str;
        });
        get("/userInfo", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new UserInfoExecutor(req.queryMap().toMap()).toString();
            System.out.println(str);
            return str;
        });
        get("/classmates", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new ClassMatesExecutor(req.queryMap().toMap()).toString();
            System.out.println(str);
            return str;
        });
        get("/salary", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new SalaryExecutor(req.queryMap().toMap()).toString();
            System.out.println(str);
            return str;
        });
        get("/getLessons", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new GetLessonsListExecutor(req.queryMap().toMap()).toString();
            System.out.println(str);
            return str;
        });
        get("/addNote", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new AddNoteExecutor(req.queryMap().toMap()).toString();
			System.out.println(str);
            return str;
        });
        get("/getNote", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new GetNoteExecutor(req.queryMap().toMap()).toString();
			System.out.println(str);
            return str;
        });
        //get("/addLK", (req, res) -> new AddLKExecutor(req.queryMap().toMap()).toString()});
        get("/getTimeTable", (req, res) ->{
            System.out.println(req.pathInfo() + " " + URLDecoder.decode(req.queryString(), "UTF-8"));
            String str = new GetTimeTableExecutor(req.queryMap().toMap()).toString();
			System.out.println(str);
            return str;
        });
    }
}
