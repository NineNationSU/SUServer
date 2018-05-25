package executors;

import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.utility.*;
import exceptions.AuthException;
import database.objects.Note;
import responses.ServerResponse;

import java.sql.SQLException;
import java.util.Map;

import static constants.Strings.*;

/**
 * Класс, обеспечивающий взаимодействие сервера и БД и предоставляющий метод для добавления заметок
 */
public class AddNoteExecutor {
    private Map<String, String[]> request;

    public AddNoteExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            String token = request.get(TOKEN)[0];
            Student student = CheckTokenExecutor.check(token);
            Note note = new Note()
                    .setGroup(student.getGroup())
                    .setLesson(request.get("lesson")[0])
                    .setText(request.get("text")[0]);
            NoteDBExecutor.addNote(note);
            return new ServerResponse("Заметка добавлена").toString();
        }catch (SQLException | NullPointerException e){
            return new ServerResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ServerResponse(AUTH_EXCEPTION).toString();
        }catch (ObjectInitException e) {
            return new ServerResponse(SERVER_EXCEPTION).toString();
        }
    }
}
