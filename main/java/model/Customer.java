package model;

public class Customer {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Integer userType;

    public Customer(String username, String password, String firstName, String lastName, Integer userType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public Customer(String username, String password, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userType = 0;
    }

    public Customer(){
        this.firstName = "";
        this.lastName = "";
        this.username = "";
        this.password = "";
        this.userType = 0;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
