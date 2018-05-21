package database.utility;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Message;
import database.objects.Student;
import utility.MessagesListWrapper;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;


public abstract class MessageDBExecutor {

    /**
     * данный метод заносит сообщение к таблицу отправителя и в таблицы получателей
     * @param message отправляемое сообщение
     * @throws SQLException при ошибке записи в БД
     * @throws ObjectInitException при невозможности создать подключение к ней
     */
    public synchronized static void sendMessage(Message message) throws SQLException, ObjectInitException {
        Integer senderId = message.getSenderId();

        String sqlRequest = "INSERT INTO suappdatabase_test.ml_id" + senderId + " SET "
                + "sender_id=?,  sender_name=?, read_state=1, `out`=1, body=?, recipients=?, `time`=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setInt(1, senderId);
        statement.setString(2, message.getSenderName());
        String body = escapeJava(message.getBody());
        statement.setString(3, body);
        StringBuilder recipients = new StringBuilder();
        for (int i = 0; i < message.getRecipients().size(); i++) {
            recipients.append(message.getRecipients().get(i).getId());
            if (i != message.getRecipients().size() - 1)
                recipients.append(" ");
        }
        statement.setString(4, recipients.toString());
        statement.setLong(5,new Date().getTime());
        statement.executeUpdate();

        sqlRequest = "SELECT MAX(`id`) FROM suappdatabase_test.ml_id" + senderId + ";";
        ResultSet set = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        set.next();
        Integer idInSenderList = set.getInt(1);
        set.close();
        for (Student st : message.getRecipients()){
            if (st.getId().equals(senderId)){
                continue;
            }
            sqlRequest = "INSERT INTO suappdatabase_test.ml_id1"/* + st.getId() */+ " SET "
                    + "sender_id=?, id_in_sender_list=?, sender_name=?, read_state=0, `out`=0, `time`=?;";
            statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);

            //senderId
            statement.setInt(1, senderId);
            //idInSenderList
            statement.setInt(2, idInSenderList);
            //senderName
            statement.setString(3, message.getSenderName());
            //time
            statement.setLong(4, message.getTime());
            statement.executeUpdate();
        }
    }

    /**
     *  Возвращает json-объект из непрочитанных сообщенйи
     */
    public synchronized static String getMessage(Integer userId) throws SQLException, ObjectInitException {
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
        //System.out.println(new MessagesListWrapper().setList(list).toString());
        return new MessagesListWrapper().setList(list).toString();
    }

    /**
     * пометка сообщений прочитанными
     */
    public synchronized static void markAsRead(Integer userId, Integer messageId) throws SQLException, IOException, ObjectInitException {
        String tableName = "suappdatabase_test.ml_id" + userId;
        String sqlRequest = "UPDATE " + tableName + " SET `read_state` = 1 WHERE id=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setInt(1, userId);
        statement.executeUpdate();
    }
}
