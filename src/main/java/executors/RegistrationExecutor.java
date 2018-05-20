package executors;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.LogPass;
import database.objects.Student;
import database.objects.StudyGroup;
import database.utility.DatabaseConnector;
import database.utility.LkDBExecutor;
import database.utility.SQLExecutor;
import exceptions.AuthException;
import responses.ErrorResponse;
import responses.OKResponse;
import ssau.lk.Grabber;
import ssau.lk.StudentInfoGrabber;
import ssau.lk.TimeTableGrabber;
import timetable.TimeTable;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import static constants.FileNames.TIMETABLE_FOLDER;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.LK_LOG_PASS_NOT_FOUND;
import static constants.Strings.SERVER_EXCEPTION;

public class RegistrationExecutor {

    public static class LKException extends Exception{
        public String getMessage(){
            return "Неверная пара логин/пароль";
        }
    }

    private Map<String, String[]> request;

    public RegistrationExecutor(Map<String, String[]> request){
        this.request = request;
    }

    @Override
    public String toString() {
        try {
            String type = request.get("type")[0];
            String login = request.get("login")[0];
            String password = request.get("password")[0];
            String birthday = request.get("birthday")[0];
            String phoneNumber = null;
            try{
                phoneNumber = request.get("phone_number")[0];
            }catch (Exception ignored){}
            if (type.equals("student")){
                Student student = new Student()
                        .setLogin(login)
                        .setPassword(password);
                if (phoneNumber != null){
                    student.setPhoneNumber(phoneNumber);
                }
                student = student
                        .setGroupManager(Boolean.parseBoolean(request.get("group_manager")[0]))
                        .setGroupPresident(Boolean.parseBoolean(request.get("group_president")[0]))
                        .setGroupProforg(Boolean.parseBoolean(request.get("group_proforg")[0]))
                        .setGender(request.get("gender")[0])
                        .setBirthday(birthday);
                String lk, rasp;
                ArrayList<String> groupInfo;
                String[] studentName;
                try {
                    lk = Grabber.getLK(login, password);

                    rasp = TimeTableGrabber.getTimeTable(lk);

                    groupInfo = StudentInfoGrabber.getGroupInfo(lk);

                    studentName = StudentInfoGrabber.getStudentName(lk);
                }catch (Exception e){
                    e.printStackTrace();
                    throw new LKException();
                }
                student.setGroup(new StudyGroup().setNumber(groupInfo.get(0)))
                        .setFirstName(studentName[0])
                        .setMiddleName(studentName[1])
                        .setLastName(studentName[2]);

                String timeTableFileName = TIMETABLE_FOLDER + student.getGroup() + ".json";
                File file = new File(timeTableFileName);
                if (!file.exists()) {
                    PrintWriter w = new PrintWriter(new FileWriter(timeTableFileName));
                    w.print(rasp);
                    return rasp;
                }
                SQLExecutor.insertNewStudent(student);
                return student.toString();
            }
        }catch (SQLException e){
            e.printStackTrace();
            return new ErrorResponse(CHECK_REQUEST_PLEASE).toString();
        }catch (LKException | IllegalObjectStateException e){
            return new ErrorResponse(e.getMessage()).toString();
        }catch (Exception e){
            return new ErrorResponse(SERVER_EXCEPTION).toString();
        }
        return new ErrorResponse("Check field [type]").toString();
    }
}
