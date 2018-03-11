import com.google.gson.Gson;

public class Lesson {
    private String discipline;
    private String type;
    private Teacher teacher;

    public String getDiscipline() {
        return discipline;
    }

    public Lesson setDiscipline(String discipline) {
        this.discipline = discipline;
        return this;
    }

    public String getType() {
        return type;
    }

    public Lesson setType(String type) {
        this.type = type;
        return this;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Lesson setTeacher(Teacher teacher) {
        this.teacher = teacher;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
