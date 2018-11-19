package dk.aau.cs.ds302e18.service;
import java.util.Objects;

public abstract class Account {

    private int userID;
    private String firstName;
    private String lastName;
    private int phonenumber;
    private String email;
    private String birthdate;
    private String address;
    private int zipCode;
    private String city;
    private String username;
    private String password;

    public Account(String firstName, String lastName, int phonenumber, String email, String birthdate, String address, int zipCode, String city, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phonenumber = phonenumber;
        this.email = email;
        this.birthdate = birthdate;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.username = username;
    }

    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getAddress() {
        return address;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhonenumber(int phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userID=" + userID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phonenumber=" + phonenumber +
                ", email='" + email + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", address='" + address + '\'' +
                ", zipCode=" + zipCode +
                ", city='" + city + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return userID == account.userID &&
                phonenumber == account.phonenumber &&
                zipCode == account.zipCode &&
                Objects.equals(firstName, account.firstName) &&
                Objects.equals(lastName, account.lastName) &&
                Objects.equals(email, account.email) &&
                Objects.equals(birthdate, account.birthdate) &&
                Objects.equals(address, account.address) &&
                Objects.equals(city, account.city) &&
                Objects.equals(username, account.username) &&
                Objects.equals(password, account.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, firstName, lastName, phonenumber, email, birthdate, address, zipCode, city, username, password);
    }
}

