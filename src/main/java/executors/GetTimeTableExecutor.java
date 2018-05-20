package executors;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.LogPass;
import database.objects.StudyGroup;
import database.utility.*;
import exceptions.AuthException;
import responses.ErrorResponse;
import ssau.lk.Grabber;
import ssau.lk.TimeTableGrabber;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Map;

import static constants.FileNames.TIMETABLE_FOLDER;
import static constants.Strings.*;

public class GetTimeTableExecutor {
    private Map<String, String[]> request;

    public GetTimeTableExecutor(Map<String, String[]> request){
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
            StudyGroup group = SQLExecutor.getGroupByStudentId(myId);
            String timeTableFileName = TIMETABLE_FOLDER + group.getNumber() + ".json";
            File file = new File(timeTableFileName);
            if (file.exists()){
                BufferedReader r = new BufferedReader(new FileReader(timeTableFileName));
                String temp;
                StringBuilder answer = new StringBuilder();
                while ((temp = r.readLine()) != null){
                    answer.append(temp);
                }
                return answer.toString();
            }
            LogPass logPass = LkDBExecutor.getStudentLKData(myId);
            if (logPass == null){
                return new ErrorResponse(LK_LOG_PASS_NOT_FOUND).toString();
            }
            String rasp = TimeTableGrabber.getTimeTable(Grabber.getLK(logPass.getLogin(), logPass.getPassword()));
            PrintWriter w = new PrintWriter(new FileWriter(timeTableFileName));
            w.print(rasp);
            return rasp;
        } catch (SQLException e){
            // TODO
            System.out.println("sql exception");
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (NullPointerException e){
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (AuthException e) {
            return new ErrorResponse(AUTH_EXCEPTION).toString();
        }catch (IOException | DatabaseConnector.CloseConnectorException | ObjectInitException | URISyntaxException | IllegalObjectStateException e) {
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
    }
}
