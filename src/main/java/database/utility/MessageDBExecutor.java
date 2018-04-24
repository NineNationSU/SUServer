package database.utility;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Message;
import database.objects.Student;
import utility.ListWrapper;

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

        DatabaseConnector.getInstance().getStatement().executeUpdate(sqlRequest);

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
            if (st.getId().equals(Integer.parseInt(senderId))){
                continue;
            }
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
        List<Message> temp_list = new ArrayList<>();
        List<Message> list = new ArrayList<>();

        String sqlRequest = "SELECT * FROM suappdatabase_test.ml_id" + userId + ";";
        ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        ResultSet res;
        while (!set.isClosed() && set.next()){
            temp_list.add(new Message(set));
        }
        set.close();

        for(Message temp : temp_list){
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

        System.out.println(list);
        System.out.println("\n\n\n");
        System.out.println(new ListWrapper<Message>().setList(list).toString());
        return new ListWrapper<Message>().setList(list).toString();
    }

    public synchronized static void markAsRead(Integer userId, Integer messageId) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String tableName = "suappdatabase_test.ml_id" + userId;
        String whereParam = "id = " + messageId;
        String sqlRequest = "UPDATE " + tableName + " SET `read_state` = 1 WHERE " + whereParam +";";
        DatabaseConnector.getInstance().getStatement().executeUpdate(sqlRequest);
    }
}
