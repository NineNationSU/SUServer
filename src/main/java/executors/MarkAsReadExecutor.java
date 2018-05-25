package executors;

import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.utility.CheckTokenExecutor;
import database.utility.MessageDBExecutor;
import exceptions.AuthException;
import responses.ServerResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import static constants.Strings.AUTH_EXCEPTION;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

/**
 * Класс, обеспечивающий взаимодействие сервера и БД и предоставляющий метод для  пометки сообщений прочитанными
 */
public class MarkAsReadExecutor {
    private Map<String, String[]> request;

    public MarkAsReadExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            Integer messageId = Integer.parseInt(request.get("message_id")[0]);
            String token = request.get(constants.Strings.TOKEN)[0];
            Student student = CheckTokenExecutor.check(token);
            MessageDBExecutor.markAsRead(student.getId(), messageId);
            return new ServerResponse("OK").toString();
        }catch (SQLException | NullPointerException e){
            return new ServerResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ServerResponse(AUTH_EXCEPTION).toString();
        }catch (IOException | ObjectInitException  e) {
            return new ServerResponse(SERVER_EXCEPTION).toString();
        }
    }
}
