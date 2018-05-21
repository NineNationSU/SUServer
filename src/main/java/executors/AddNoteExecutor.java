package executors;

import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.utility.*;
import exceptions.AuthException;
import notes.Note;
import responses.ErrorResponse;
import responses.OKResponse;

import java.sql.SQLException;
import java.util.Map;

import static constants.Strings.AUTH_EXCEPTION;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

public class AddNoteExecutor {
    private Map<String, String[]> request;

    public AddNoteExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            String token = request.get("token")[0];
            Student student = CheckTokenExecutor.check(token);
            Note note = new Note()
                    .setGroup(student.getGroup())
                    .setLesson(request.get("lesson")[0])
                    .setText(request.get("text")[0]);
            NoteDBExecutor.addNote(note);
            return new OKResponse().toString();
        }catch (SQLException | NullPointerException e){
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ErrorResponse(AUTH_EXCEPTION).toString();
        }catch (ObjectInitException e) {
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
