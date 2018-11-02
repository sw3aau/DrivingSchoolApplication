package dk.aau.cs.ds302e18.app.auth;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserManagerTest {
    private ArrayList<User> userList;
    private UserManager userManager;

    @Before
    public void setUp() throws Exception {
        userManager = new UserManager();
        User user1 = new User();
        user1.setUsername("username1");

        User user2 = new User();
        user2.setUsername("username2");

        User user3 = new User();
        user3.setUsername("username3");

        userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
    }

    @Test
    public void saveUsernamesAsString() {
        String combinedUsernames = userManager.saveUsernamesAsString(userList);
        assertEquals("username1:username2:username3:", combinedUsernames);
    }

    @Test
    public void saveStringAsUsers() {
        String combinedUsernames = userManager.saveUsernamesAsString(userList);
        ArrayList<User> returnedUserList = userManager.saveStringAsUsers(combinedUsernames);
        /* Checks that the list has 3 objects */
        assertEquals(3, returnedUserList.size());
        /* Checks that every user has the proper username */
        String usernamesFromEachAccount = "";
        for(User usernames : returnedUserList) {
            usernamesFromEachAccount += usernames.getUsername() + " ";
        }
        assertEquals("username1 username2 username3 ", usernamesFromEachAccount);
    }
}