package dk.aau.cs.ds302e18.website;

public class Instructor extends Account {

    private boolean isAdmin;

    public Instructor(String firstName, String lastName, String phonenumber, String email, String birthdate, String address, String zipCode, String city, String username) {
        super(firstName, lastName, phonenumber, email, birthdate, address, zipCode, city, username);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
