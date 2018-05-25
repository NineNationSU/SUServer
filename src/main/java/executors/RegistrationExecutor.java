package executors;

import com.google.gson.Gson;
import database.exceptions.IllegalObjectStateException;
import database.objects.Student;
import database.objects.StudyGroup;
import database.utility.SQLExecutor;
import responses.ServerResponse;
import ssau.lk.Grabber;
import ssau.lk.StudentInfoGrabber;
import ssau.lk.TimeTableGrabber;
import utility.MD5Utility;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import static constants.FileNames.TIMETABLE_FOLDER;
import static constants.Strings.CHECK_REQUEST_PLEASE;
import static constants.Strings.SERVER_EXCEPTION;

/**
 * Класс, обеспечивающий взаимодействие сервера и БД и предоставляющий метод для регистрации студента
 */
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

    /**
     *
     * @return объект, характеризующий харегистрированного студента
     */
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
                        .setGroupManager(Integer.parseInt(request.get("group_manager")[0]))
                        .setGroupPresident(Integer.parseInt(request.get("group_president")[0]))
                        .setGroupProforg(Integer.parseInt(request.get("group_proforg")[0]))
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
                        .setFirstName(studentName[1])
                        .setMiddleName(studentName[2])
                        .setLastName(studentName[0]);

                String timeTableFileName = TIMETABLE_FOLDER + student.getGroupNumber() + ".json";
                File file = new File(timeTableFileName);
                if (!file.exists()) {
                    PrintWriter w = new PrintWriter(new FileWriter(timeTableFileName));
                    w.print(rasp);
                    return rasp;
                }
                student.setToken(MD5Utility.getMD5(login + password));
                SQLExecutor.insertNewStudent(student);
                return new Gson().toJson(student);
            }
        }catch (NullPointerException | SQLException e) {
            if (e.getMessage().contains("Duplicate")){
                return new ServerResponse("Повторная регистрация невозможна").toString();
            }
            e.printStackTrace();
            return new ServerResponse(CHECK_REQUEST_PLEASE).toString();
        } catch (LKException | IllegalObjectStateException e){
            return new ServerResponse(e.getMessage()).toString();
        }catch (Exception e){
            return new ServerResponse(SERVER_EXCEPTION).toString();
        }
        return new ServerResponse("Check field [type]").toString();
    }
}
