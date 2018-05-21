package executors;

import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.utility.CheckTokenExecutor;
import database.utility.DatabaseConnector;
import database.utility.NoteDBExecutor;
import exceptions.AuthException;
import responses.ErrorResponse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static constants.Strings.AUTH_EXCEPTION;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

public class GetNoteExecutor {
    private Map<String, String[]> request;

    public GetNoteExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            String lesson = request.get("lesson")[0];
            String token = request.get("token")[0];
            Student student = CheckTokenExecutor.check(token);
            return NoteDBExecutor.getNote(lesson, student.getGroupNumber());
        } catch (SQLException e){
            // TODO
            System.out.println("sql exception");
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (NullPointerException e){
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ErrorResponse(AUTH_EXCEPTION).toString();
        }catch (ObjectInitException e) {
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
