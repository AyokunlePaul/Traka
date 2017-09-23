package i.am.eipeks.traka.util;


public class User {

    private String phoneNumber;
    private String email;
    private String fullName;

    public User(){}

    public User(String fullName, String email, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }
}
