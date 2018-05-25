package executors;

import com.google.gson.Gson;
import responses.ServerResponse;
import timetable.*;

import java.util.Map;

import static constants.Strings.SERVER_EXCEPTION;

/**
 * Класс, обеспечивающий взаимодействие сервера и БД и предоставляющий метод для получения списка предметов
 */
public class GetLessonsListExecutor {
    private Map<String, String[]> request;

    public GetLessonsListExecutor(Map<String, String[]> request) {
        this.request = request;
    }

    private String addDiscipline(String discipline, String answer){
        if(answer.isEmpty()){
            answer += discipline;
        }
        else if(!answer.contains(discipline)){
            answer += ";" + discipline;
        }
        return answer;
    }

    @Override
    public String toString() {
        try {
            String timeTable = new GetTimeTableExecutor(request).toString();
            ServerResponse error = new Gson().fromJson(timeTable, ServerResponse.class);
            if(error.getResponse() != null){
                return error.toString();
            }
            TimeTable t = new Gson().fromJson(timeTable, TimeTable.class);

            String answer = "";
            for (int i = 0; i < t.getWeeks().size(); i++) {
                WeekTimeTable week = t.getWeeks().get(i);
                for (int j = 0; j < week.getDays().size(); j++) {
                    DayTimeTable day = week.getDays().get(j);
                    for (int k = 0; k < day.getCouple().size(); k++) {
                        Couple couple = day.getCouple().get(k);
                        if (couple != null){
                            if (couple.getBySubgroups()){
                                Lesson lesson1 = couple.getFirstSubgroup(),
                                        lesson2 = couple.getSecondSubgroup();
                                if(lesson1 != null){
                                    answer = addDiscipline(lesson1.getDiscipline(), answer);
                                }
                                if(lesson2 != null){
                                    answer = addDiscipline(lesson2.getDiscipline(), answer);
                                }
                            }else{
                                Lesson lesson = couple.getAllGroup();
                                if(lesson != null){
                                    answer = addDiscipline(lesson.getDiscipline(), answer);
                                }
                            }
                        }
                    }
                }
            }
            return answer;
        }catch (NullPointerException e) {
            return new ServerResponse(e.getMessage()).toString();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerResponse(SERVER_EXCEPTION).toString();
        }
    }
}
