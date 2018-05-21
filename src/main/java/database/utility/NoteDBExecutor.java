package database.utility;

import database.exceptions.ObjectInitException;
import notes.Note;
import utility.NoteListWrapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.text.StringEscapeUtils.escapeJava;


public abstract class NoteDBExecutor {
    /**
     * @return id найденной заметки или -1, если заметка не найдена
     */
    public static Integer findNote(Note note) throws SQLException, ObjectInitException {
        String sqlRequest = "SELECT * FROM suappdatabase_test.notes WHERE group_number=? AND lesson=? AND text=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setString(1, note.getGroup().getNumber());
        statement.setString(2, note.getLesson());
        statement.setString(3, escapeJava(note.getText()));
        ResultSet res = statement.executeQuery();
        if (res.next()){
            return res.getInt("id");
        }else {
            return -1;
        }
    }

    public static void addNote(Note note) throws SQLException, ObjectInitException {
        String sqlRequest = "INSERT INTO suappdatabase_test.notes SET  group_number=?, lesson=?, text=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setString(1, note.getGroup().getNumber());
        statement.setString(2, note.getLesson());
        statement.setString(3, note.getText());
        statement.executeUpdate();
    }

    public static String getNote(String lesson, String groupNumber) throws SQLException, ObjectInitException {
        String sqlRequest = "SELECT * FROM suappdatabase_test.notes WHERE group_number=? AND lesson=?;";
        PreparedStatement statement = DatabaseConnector.getInstance().getConnection().prepareStatement(sqlRequest);
        statement.setString(1, groupNumber);
        statement.setString(2, lesson);
        ResultSet res = statement.executeQuery();
        List<Note> notes = new ArrayList<>();
        while (res.next()){
            notes.add(new Note(res));
        }
        System.out.println(notes);
        return  new NoteListWrapper().setList(notes).toString();

    }

}
