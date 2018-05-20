package executors;

import database.exceptions.ObjectInitException;
import database.utility.CheckTokenExecutor;
import database.utility.DatabaseConnector;
import database.utility.NoteDBExecutor;
import database.utility.SQLExecutor;
import exceptions.AuthException;
import notes.Note;
import responses.ErrorResponse;
import responses.OKResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
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
            Integer myId = Integer.parseInt(request.get("my_id")[0]);
            String token = request.get("token")[0];
            if (!CheckTokenExecutor.check(token)) {
                throw new AuthException();
            }
            Note note = new Note()
                    .setDate(request.get("date")[0])
                    .setGroup(SQLExecutor.getGroupByStudentId(myId));
            return NoteDBExecutor.getNote(note);
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
