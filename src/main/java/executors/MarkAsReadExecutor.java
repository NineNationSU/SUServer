package executors;

import database.exceptions.ObjectInitException;
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
            Integer myId = Integer.parseInt(request.get("my_id")[0]);
            Integer messageId = Integer.parseInt(request.get("message_id")[0]);
            String token = request.get("token")[0];
            if (!CheckTokenExecutor.check(myId, token)) {
                throw new AuthException();
            }
            MessageDBExecutor.markAsRead(myId, messageId);

            return new OKResponse().toString();
        }catch (SQLException | NullPointerException e){
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ErrorResponse(AUTH_EXCEPTION).toString();
        }catch (IOException | DatabaseConnector.CloseConnectorException | ObjectInitException  e) {
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
