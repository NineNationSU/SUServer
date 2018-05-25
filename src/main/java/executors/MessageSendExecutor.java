package executors;

import database.exceptions.ObjectInitException;
import database.objects.Message;
import database.objects.Student;
import database.utility.CheckTokenExecutor;
import database.utility.MessageDBExecutor;
import database.utility.SQLExecutor;
import exceptions.AuthException;
import responses.ServerResponse;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static constants.Strings.AUTH_EXCEPTION;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

public class MessageSendExecutor {
    private Map<String, String[]> request;

    public MessageSendExecutor(Map<String, String[]> request){
        this.request = request;
    }

    private List<Student> getRecipientsFromQueryParam(String groupNumber) throws SQLException, ObjectInitException, IOException {
        ArrayList<Student> answer = new ArrayList<>();

        System.out.println("\t\t" + groupNumber);
        ResultSet set = SQLExecutor.getStudentsByGroup(groupNumber);
        while (set.next()){
            answer.add(new Student().setId(set.getInt("id")));
        }
        set.close();
        return answer;
    }

    @Override
    public String toString() {
        try {
            String token = request.get(constants.Strings.TOKEN)[0];
            Student student = CheckTokenExecutor.check(token);
            if (!student.isGroupManager() && !student.isGroupPresident() && !student.isGroupProforg()){
                return new ServerResponse("Вашему типу пользователя запрещено рассылать сообщения").toString();
            }
            String body = request.get("body")[0];
            List<Student> recipients = getRecipientsFromQueryParam(request.get("recipients")[0]);
            Long time = new Date().getTime();
            System.out.println(body);
            Message outMessage = new Message()
                    .setOut(1)
                    .setBody(body)
                    .setReadState(1)
                    .setRecipients(recipients)
                    .setSenderId(student.getId())
                    .setSenderName(student.getLastName() + " " + student.getFirstName())
                    .setTime(time);
            MessageDBExecutor.sendMessage(outMessage);
            return new ServerResponse("Отправлено").toString();

        }catch (SQLException | NullPointerException e){
            e.printStackTrace();
            return new ServerResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ServerResponse(AUTH_EXCEPTION).toString();
        }catch (Exception  e) {
            return new ServerResponse(SERVER_EXCEPTION).toString();
        }
    }
}
