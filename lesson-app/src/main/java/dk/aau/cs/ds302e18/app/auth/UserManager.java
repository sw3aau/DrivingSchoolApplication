package dk.aau.cs.ds302e18.app.auth;

import java.util.ArrayList;

public class UserManager {
    /* 1. Retrieve an list of usernames from the database.
     * 2. Save the names from all the objects as an string.   /DO FIRST
     * 3. Send this as an student list to the database.
     * 4. Retrieve this studentList from the db, split it again and turn it into user objects again. DO FIRST */

    public String saveUsernamesAsString(ArrayList<User> userList) {
        String combinedString = "";
        for(User user : userList) {
            combinedString += user.getUsername() + ":";
        }
        return combinedString;
    }

    /* Separates a string into usernames and creates an list of users with those usernames. */
    public ArrayList<User> saveStringAsUsers(String stringWithUsernames) {
        ArrayList<User> userList = new ArrayList<>();
        String[] parts = stringWithUsernames.split(":");
        for(int i = 0; i< parts.length; i++){
            userList.add(new User());
            userList.get(i).setUsername(parts[i]);
        }
        return userList;
    }
}
