package executors;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.utility.*;
import exceptions.AuthException;
import responses.ServerResponse;
import ssau.lk.Grabber;
import ssau.lk.TimeTableGrabber;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Map;

import static constants.FileNames.TIMETABLE_FOLDER;
import static constants.Strings.*;

/**
 * Класс, обеспечивающий взаимодействие сервера и БД и предоставляющий метод для получения расписания
 */
public class GetTimeTableExecutor {
    private Map<String, String[]> request;

    public GetTimeTableExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString(){
        try {
            String token = request.get(constants.Strings.TOKEN)[0];
            Student student = CheckTokenExecutor.check(token);
            String timeTableFileName = TIMETABLE_FOLDER + student.getGroupNumber() + ".json";
            File file = new File(timeTableFileName);
            if (file.exists()){
                BufferedReader r = new BufferedReader(
                        new InputStreamReader(
                            new FileInputStream(
                                    new File(timeTableFileName)), Charset.forName("UTF-8")));
                String temp;
                StringBuilder answer = new StringBuilder();
                while ((temp = r.readLine()) != null){
                    answer.append(temp);
                }
                r.close();
                return answer.toString();
            }
            String rasp = TimeTableGrabber.getTimeTable(Grabber.getLK(student.getLogin(), student.getPassword()));
            PrintWriter w = new PrintWriter(new FileWriter(timeTableFileName));
            w.print(rasp);
            w.close();
            return rasp;
        } catch (SQLException | NullPointerException e){
            return new ServerResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ServerResponse(AUTH_EXCEPTION).toString();
        }catch (IOException | ObjectInitException | URISyntaxException | IllegalObjectStateException e) {
            return new ServerResponse(SERVER_EXCEPTION).toString();
        }
    }
}
