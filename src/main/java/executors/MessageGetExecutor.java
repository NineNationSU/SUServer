package executors;

import constants.Strings;
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

import static constants.Strings.AUTH_EXCEPTION;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

public class MessageGetExecutor {
    private Map<String, String[]> request;

    public MessageGetExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            String token = request.get("token")[0];
            Student student = CheckTokenExecutor.check(token);
            return MessageDBExecutor.getMessage(student.getId());

        }catch (SQLException | NullPointerException e){
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ErrorResponse(AUTH_EXCEPTION).toString();
        }catch (ObjectInitException  e) {
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
