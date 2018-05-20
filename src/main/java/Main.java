
import executors.*;

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
        get("/classmates", (req, res) ->{
            System.out.println(res);
            String str = new ClassMatesExecutor(req.queryMap().toMap()).toString();
            System.out.println(str);
            return str;
        });
        get("/salary", (req, res) ->{
            System.out.println(res);
            String str = new SalaryExecutor(req.queryMap().toMap()).toString();
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
