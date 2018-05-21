package executors;

import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.utility.CheckTokenExecutor;
import database.utility.DatabaseConnector;
import database.utility.MessageDBExecutor;
import exceptions.AuthException;
import responses.ErrorResponse;
import responses.OKResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import static constants.Strings.AUTH_EXCEPTION;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

public class MarkAsReadExecutor {
    private Map<String, String[]> request;

    public MarkAsReadExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            Integer messageId = Integer.parseInt(request.get("message_id")[0]);
            String token = request.get("token")[0];
            Student student = CheckTokenExecutor.check(token);
            MessageDBExecutor.markAsRead(student.getId(), messageId);
            return new OKResponse().toString();
        }catch (SQLException | NullPointerException e){
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ErrorResponse(AUTH_EXCEPTION).toString();
        }catch (IOException | ObjectInitException  e) {
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
