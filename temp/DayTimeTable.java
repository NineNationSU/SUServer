import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DayTimeTable {
    private int dayOfWeek;
    private List<Lesson> lessons;

    public DayTimeTable() {
        lessons = new ArrayList<>();
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public DayTimeTable setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public DayTimeTable setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
