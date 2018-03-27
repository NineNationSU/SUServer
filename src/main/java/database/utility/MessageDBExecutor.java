package database.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Message;
import database.objects.Student;
import database.utility.DatabaseConnector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class MessageDBExecutor {

    /**
     * Возвращает идентификатор отправленного сообщения
     */
    public synchronized static String sendMessage(Message message) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException, IllegalObjectStateException {
        String senderId = String.valueOf(message.getSenderId());
        String sqlRequest = "INSERT INTO suappdatabase_test.ml_id" + senderId + " SET " + message.toSQLOutputMessage() + ";";

        // TODO
        System.out.println(sqlRequest);

        DatabaseConnector.getInstance().getStatement().execute(sqlRequest);

        sqlRequest = "SELECT MAX(`id`) FROM suappdatabase_test.ml_id" + senderId + ";";
        ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        set.next();
        Integer idInSenderList = set.getInt(1);
        set.close();
        Message messageForRecipients = new Message();
        messageForRecipients.setTime(message.getTime());
        messageForRecipients.setIdInSenderList(idInSenderList);
        messageForRecipients.setSenderId(Integer.parseInt(senderId));
        for (Student st : message.getRecipients()){

            sqlRequest = "INSERT INTO suappdatabase_test.ml_id" + st.getId()
                    + " SET " + messageForRecipients.toSQLReceivedMessage() + ";";

            DatabaseConnector.getInstance().getStatement().execute(sqlRequest);
        }

        return idInSenderList.toString();
    }

    /**
     *  Возвращает json-объект из непрочитанных сообщенйи
     */
    public synchronized static String getMessage(Integer userId) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        List<Message> list = new ArrayList<>();

        String sqlRequest = "SELECT * FROM suappdatabase_test.ml_id" + userId + ";";
        ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        ResultSet res;

        while (!set.isClosed() && set.next()){
            Message temp = new Message(set);
            if (!temp.getSenderId().equals(userId) || temp.getOut() == 0) {
                sqlRequest = "SELECT * FROM suappdatabase_test.ml_id" + temp.getSenderId() +
                        " WHERE id = " + temp.getIdInSenderList() + ";";

                res = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
                if (res.next()) {
                    temp.setBody(res.getString("body"));
                    list.add(temp);
                }
                res.close();

            }else{
                list.add(temp);
            }

        }
        set.close();

        return  new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create().toJson(list);
    }
}
