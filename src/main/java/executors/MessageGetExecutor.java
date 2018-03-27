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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MessageGetExecutor {
    private Map<String, String[]> request;

    public MessageGetExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            Integer myId = Integer.parseInt(request.get("my_id")[0]);
            String token = request.get("token")[0];
            if (!CheckTokenExecutor.check(myId, token)) {
                throw new AuthException("Невалидный токен");
            }
            return MessageDBExecutor.getMessage(myId);

        }catch (AuthException | SQLException | IOException | DatabaseConnector.CloseConnectorException | ObjectInitException  e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage()).toString();
        }catch (NullPointerException e){
            return new ErrorResponse("Fill all fields").toString();
        }
    }
}
