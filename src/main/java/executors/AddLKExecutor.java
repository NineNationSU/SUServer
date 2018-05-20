package executors;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.LogPass;
import database.objects.Student;
import database.objects.StudyGroup;
import database.utility.CheckTokenExecutor;
import database.utility.DatabaseConnector;
import database.utility.LkDBExecutor;
import database.utility.SQLExecutor;
import exceptions.AuthException;
import responses.ErrorResponse;
import responses.OKResponse;
import ssau.lk.Grabber;
import ssau.lk.TimeTableGrabber;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;

import static constants.FileNames.TIMETABLE_FOLDER;
import static constants.Strings.*;

public class AddLKExecutor {
    private Map<String, String[]> request;

    public AddLKExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString(){
        try {
            Integer myId = Integer.parseInt(request.get("my_id")[0]);
            String token = request.get("token")[0];
            if (!CheckTokenExecutor.check(token)) {
                throw new AuthException();
            }
            String login = request.get("login")[0];
            String password = request.get("password")[0];

            LkDBExecutor.insertStudentData(new Student().setId(myId).setLogin(login).setPassword(password));

            return new OKResponse().toString();
        } catch (SQLException e){
            // TODO
            System.out.println("sql exception");
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (NullPointerException e){
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ErrorResponse(AUTH_EXCEPTION).toString();
        }catch (IOException | DatabaseConnector.CloseConnectorException | ObjectInitException e) {
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
