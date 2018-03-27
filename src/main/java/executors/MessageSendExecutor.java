package executors;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Message;
import database.objects.Student;
import database.utility.CheckTokenExecutor;
import database.utility.DatabaseConnector;
import database.utility.MessageDBExecutor;
import database.utility.SQLExecutor;
import exceptions.AuthException;
import responses.ErrorResponse;
import responses.OKResponse;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MessageSendExecutor {
    private Map<String, String[]> request;

    public MessageSendExecutor(Map<String, String[]> request){
        this.request = request;
    }

    private List<Student> getRecipientsFromQueryParam(String[] groupNumbers) throws SQLException, ObjectInitException, DatabaseConnector.CloseConnectorException, IOException {
        ArrayList<Student> answer = new ArrayList<>();

        for(String groupNumber : groupNumbers){
            System.out.println("\t\t" + groupNumber);
            ResultSet set = SQLExecutor.getStudentsByGroup(groupNumber);
            while (set.next()){
                answer.add(new Student().setId(set.getInt("id")));
            }
            set.close();
        }
        return answer;
    }

    @Override
    public String toString() {
        try {
            Integer senderId = Integer.parseInt(request.get("my_id")[0]);
            String token = request.get("token")[0];
            if (!CheckTokenExecutor.check(senderId, token)) {
                throw new AuthException("Невалидный токен");
            }
            String body = request.get("body")[0];
            List<Student> recipients = getRecipientsFromQueryParam(request.get("recipients")[0].split(","));
            Long time = new Date().getTime();

            Message outMessage = new Message()
                    .setOut(1)
                    .setBody(body)
                    .setReadState(1)
                    .setRecipients(recipients)
                    .setSenderId(senderId)
                    .setTime(time);

            MessageDBExecutor.sendMessage(outMessage);

            return new OKResponse().toString();

        }catch (AuthException | SQLException | IOException | DatabaseConnector.CloseConnectorException | ObjectInitException | IllegalObjectStateException e) {
            System.out.println(e.getMessage());
            return new ErrorResponse(e.getMessage()).toString();
        }catch (NullPointerException e){
            return new ErrorResponse("Fill all fields").toString();
        }
    }
}
