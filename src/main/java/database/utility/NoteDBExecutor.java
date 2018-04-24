package database.utility;

import database.exceptions.ObjectInitException;
import notes.Note;
import utility.ListWrapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class NoteDBExecutor {
    /**
     * @return id найденной заметки или -1, если заметка не найдена
     */
    public static Integer findNote(Note note) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String sqlRequest = "SELECT * FROM suappdatabase_test.notes WHERE " +
                "group_number = '" + note.getGroup().getNumber() + "' AND " +
                "`date` = '" + note.getDate() + "' AND " +
                "lesson_number = " + note.getLessonNumber() + ";";

        // TODO
        System.out.println(sqlRequest);
        ResultSet res = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        if (res.next()){
            return res.getInt("id");
        }else {
            return -1;
        }
    }

    public static void updateNote(Integer id, String title, String text) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String sqlRequest = "UPDATE suappdatabase_test.notes SET " +
                "title = '" + title + "'," +
                "text = '" + text + "' WHERE id = " + id + ";";
        DatabaseConnector.getInstance().getStatement().executeUpdate(sqlRequest);
    }

    public static void addNote(Note note) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String sqlRequest = "INSERT INTO suappdatabase_test.notes SET " +
                "group_number = '" + note.getGroup().getNumber()  + "', " +
                "`date` = '" + note.getDate() + "', " +
                "lesson_number = " + note.getLessonNumber() + ", " +
                "title = '" + note.getTitle() + "', " +
                "text = '" + note.getText() + "';";
        // TODO
        System.out.println(sqlRequest);
        DatabaseConnector.getInstance().getStatement().executeUpdate(sqlRequest);
        // TODO
        System.out.println("111111111111111111111111111111111");
    }

    public static String getNote(Note note) throws DatabaseConnector.CloseConnectorException, SQLException, IOException, ObjectInitException {
        String sqlRequest = "SELECT * FROM suappdatabase_test.notes WHERE " +
                "group_number = '" + note.getGroup().getNumber() + "' AND " +
                "`date` = '" + note.getDate() + "';";
        System.out.println(sqlRequest);
        ResultSet res = DatabaseConnector.getInstance().getStatement().executeQuery(sqlRequest);
        List<Note> notes = new ArrayList<>();
        while (res.next()){
            System.out.println(res.getInt("id"));
            notes.add(new Note(res));
        }
        System.out.println(notes);
        return  new ListWrapper<Note>().setList(notes).toString();

    }

}
