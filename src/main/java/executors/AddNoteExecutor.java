package executors;

import database.exceptions.ObjectInitException;
import database.objects.StudyGroup;
import database.utility.*;
import exceptions.AuthException;
import notes.Note;
import responses.ErrorResponse;
import responses.OKResponse;

import java.io.IOException;
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
            Integer myId = Integer.parseInt(request.get("my_id")[0]);
            String token = request.get("token")[0];
            if (!CheckTokenExecutor.check(myId, token)) {
                throw new AuthException();
            }
            Note note = new Note()
                    .setDate(request.get("date")[0])
                    .setGroup(SQLExecutor.getGroupByStudentId(myId))
                    .setLessonNumber(Integer.parseInt(request.get("lesson_number")[0]))
                    .setTitle(request.get("title")[0])
                    .setText(request.get("text")[0]);
            Integer noteId;

            if(!(noteId = NoteDBExecutor.findNote(note)).equals(-1)){
                System.out.println("update");
                NoteDBExecutor.updateNote(noteId, note.getTitle(), note.getText());
            }else {
                NoteDBExecutor.addNote(note);
            }
            return new OKResponse().toString();
        }catch (SQLException | NullPointerException e){
            System.out.println(e.getMessage());
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ErrorResponse(AUTH_EXCEPTION).toString();
        }catch (IOException | DatabaseConnector.CloseConnectorException | ObjectInitException e) {
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
