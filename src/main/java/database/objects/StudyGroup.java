package database.objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class StudyGroup {

    private String number;

    private String faculty;

    /**
     * Бакалавр, магистр или специалист
     */
    private String graduation;

    private Byte course;

    /**
     * Сокращенное название специальности
     */
    private String specialization;

    @SerializedName("group_president_id")
    private Integer groupPresidentId;

    @SerializedName("group_proforg_id")
    private Integer groupProforgId;

    public String getNumber() {
        return number;
    }

    public StudyGroup setNumber(String number) {
        this.number = number;
        return this;
    }

    public String getFaculty() {
        return faculty;
    }

    public StudyGroup setFaculty(String faculty) {
        this.faculty = faculty;
        return this;
    }

    public String getGraduation() {
        return graduation;
    }

    public StudyGroup setGraduation(String graduation) {
        this.graduation = graduation;
        return this;
    }

    public Byte getCourse() {
        return course;
    }

    public StudyGroup setCourse(Byte course) {
        this.course = course;
        return this;
    }

    public String getSpecialization() {
        return specialization;
    }

    public StudyGroup setSpecialization(String specialization) {
        this.specialization = specialization;
        return this;
    }

    public Integer getGroupPresidentId() {
        return groupPresidentId;
    }

    public StudyGroup setGroupPresidentId(Integer groupPresidentId) {
        this.groupPresidentId = groupPresidentId;
        return this;
    }

    public Integer getGroupProforgId() {
        return groupProforgId;
    }

    public StudyGroup setGroupProforgId(Integer groupProforgId) {
        this.groupProforgId = groupProforgId;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
