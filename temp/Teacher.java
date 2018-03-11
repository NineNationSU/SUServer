import com.google.gson.Gson;

public class Teacher {
    private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public Teacher(){}

    public int getId() {
        return id;
    }

    public Teacher setId(int id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Teacher setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Teacher setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Teacher setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Teacher setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Teacher setEmail(String email) {
        this.email = email;
        return this;
    }

    public Teacher setFullName(String fullName){
        if(fullName != null && !fullName.isEmpty()) {
            String[] temp = fullName.split(" ");
            lastName = temp[0];
            firstName = temp[1];
            middleName = temp[2];
        }
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
