package dk.aau.cs.ds302e18.rest.api;
public class Student extends Account {


    public Student(String firstName, String lastName, int phonenumber, String email, String birthdate, String address, int zipCode, String city, String username) {
        super(firstName, lastName, phonenumber, email, birthdate, address, zipCode, city, username);
    }
}
