
import database.exceptions.IllegalObjectStateException;
import database.utility.DatabaseConnector;
import ssau.lk.Grabber;
import ssau.lk.TimeTableGrabber;
import timetable.TimeTable;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnector connector = new DatabaseConnector();
            connector.close();
        } catch (SQLException | IOException  e) {
            e.printStackTrace();
        }
    }
}
