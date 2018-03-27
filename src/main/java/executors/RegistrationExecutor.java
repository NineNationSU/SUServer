package executors;

import database.exceptions.IllegalObjectStateException;
import database.exceptions.ObjectInitException;
import database.objects.Student;
import database.objects.StudyGroup;
import database.utility.DatabaseConnector;
import database.utility.SQLExecutor;
import responses.ErrorResponse;
import responses.OKResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class RegistrationExecutor {

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
            String firstName = request.get("first_name")[0];
            String middleName = request.get("middle_name")[0];
            String lastName = request.get("last_name")[0];
            String phoneNumber = null;
            try{
                phoneNumber = request.get("phone_nymber")[0];
            }catch (Exception e){}
            if (type.equals("student")){
                Student student = new Student()
                        .setFirstName(firstName)
                        .setMiddleName(middleName)
                        .setLastName(lastName)
                        .setLogin(login)
                        .setPassword(password);
                if (phoneNumber != null){
                    student.setPhoneNumber(phoneNumber);
                }
                student = student
                        .setGroupManager(Boolean.parseBoolean(request.get("group_manager")[0]))
                        .setGroupPresident(Boolean.parseBoolean(request.get("group_president")[0]))
                        .setGroupProforg(Boolean.parseBoolean(request.get("group_proforg")[0]))
                        .setGroup(new StudyGroup().setNumber(request.get("group")[0]))
                        .setGender(request.get("gender")[0])
                        .setBirthday(request.get("birthday")[0]);
                SQLExecutor.insertNewStudent(student);
                return new OKResponse().toString();
            }
        }catch (DatabaseConnector.CloseConnectorException |IOException
                | IllegalObjectStateException | SQLException | ObjectInitException e) {
            return new ErrorResponse(e.getMessage()).toString();
        }catch (NullPointerException e){
            return new ErrorResponse("Fill all fields").toString();
        }
        return new ErrorResponse("Check field [type]").toString();
    }
}
