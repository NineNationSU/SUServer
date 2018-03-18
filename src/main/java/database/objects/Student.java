package database.objects;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import database.exceptions.IllegalObjectStateException;

/**
 * Данный класс описывает запись в таблице suappdatabase_test.students
 */
public class Student {

    private Integer id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("middle_name")
    private String middleName;

    @SerializedName("last_name")
    private String lastName;

    private String birthday;

    private String gender;

    @SerializedName("phone_number")
    private String phoneNumber;

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

    /**
     * Полный номер группы, например <code>"6209-010302D"</code>
     */
    private String group;

    @SerializedName("group_president")
    private Boolean groupPresident;

    @SerializedName("group_proforg")
    private Boolean groupProforg;

    @SerializedName("group_manager")
    private Boolean groupManager;


    public Integer getId() {
        return id;
    }

    public Student setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Student setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Student setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Student setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public Student setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Student setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Student setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    /**
     *
     * @return строка специального вида для занесения записи в базу данных
     * @throws IllegalObjectStateException если заполнены не все поля
     */
    public String toSQLInsertString() throws IllegalObjectStateException {
        /*if (firstName == null || middleName == null
                || lastName == null || birthday == null
                || gender == null || faculty == null
                || graduation == null || course == null
                || specialization == null || group == null){
            throw new IllegalObjectStateException("Заполнены не все обязательные поля объекта 'Студент'");
        }*/
        StringBuilder answer = new StringBuilder();
        answer.append("firstName='").append(firstName).append('\'');
        answer.append(", middleName='").append(middleName).append('\'');
        answer.append(", lastName='").append(lastName).append('\'');
        answer.append(", birthday='").append(birthday).append('\'');
        answer.append(", gender='").append(gender).append('\'');
        if (phoneNumber != null) {
            answer.append(", phoneNumber='").append(phoneNumber).append('\'');
        }
        answer.append(", faculty='").append(faculty).append('\'');
        answer.append(", graduation='").append(graduation).append('\'');
        answer.append(", course=").append(course);
        answer.append(", specialization='").append(specialization).append('\'');
        answer.append(", group='").append(group).append('\'');
        if (groupPresident != null){
            answer.append(", groupPresident=").append(groupPresident);
        }
        if (groupProforg != null){
            answer.append(", groupProforg=").append(groupProforg);
        }
        if (groupManager != null){
            answer.append(", groupManager=").append(groupManager);
        }
        return answer.toString();
    }

    public static Student loadFromJson(String jsonString){
        return new Gson().fromJson(jsonString, Student.class);
    }
}
