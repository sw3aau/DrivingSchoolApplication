package dk.aau.cs.ds302e18.app;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class RegisterUser
{
    Connection conn = null;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String birthday;
    private String address;
    private String zipCode;
    private String city;

    public RegisterUser(String username, String password, String firstName, String lastName, String phoneNumber,
                        String email, String birthday, String address, String zipCode, String city) throws SQLException
    {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthday = birthday;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;

        ResourceBundle reader = ResourceBundle.getBundle("application");
        String mysqlUrl = reader.getString("spring.datasource.url");
        String mysqlUsername = reader.getString("spring.datasource.username");
        String mysqlPassword = reader.getString("spring.datasource.password");

        try
        {
            // create a mysql database connection
            String myDriver = "org.gjt.mm.mysql.Driver";
            Class.forName(myDriver);
            conn = DriverManager.getConnection(mysqlUrl, mysqlUsername, mysqlPassword);

            Statement st = conn.createStatement();

            // TODO: "re-type password" in the register account template does nothing
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);

            conn.setAutoCommit(false);

            String insertAccountSQL = "INSERT INTO account"
                    + "(address, birthday, city, email, first_name, last_name, phone_number, username, zip) VALUES"
                    + "(?,?,?,?,?,?,?,?,?)";


            String insertUserSQL = "INSERT INTO user"
                    + "(is_active, username, password) VALUES"
                    + "(?,?,?)";

            String insertAuthUserGroupSQL = "INSERT INTO auth_user_group"
                    + "(auth_group, username) VALUES"
                    + "(?,?)";

            PreparedStatement preparedStatementInsert = conn.prepareStatement(insertUserSQL);
            preparedStatementInsert.setInt(1, 1);
            preparedStatementInsert.setString(2, username);
            preparedStatementInsert.setString(3, passwordEncoder.encode(password));
            preparedStatementInsert.executeUpdate();

            preparedStatementInsert = conn.prepareStatement(insertAccountSQL);
            preparedStatementInsert.setString(1, address);
            preparedStatementInsert.setString(2, birthday);
            preparedStatementInsert.setString(3, city);
            preparedStatementInsert.setString(4, email);
            preparedStatementInsert.setString(5, firstName);
            preparedStatementInsert.setString(6, lastName);
            preparedStatementInsert.setString(7, phoneNumber);
            preparedStatementInsert.setString(8, username);
            preparedStatementInsert.setString(9, zipCode);
            preparedStatementInsert.executeUpdate();

            preparedStatementInsert = conn.prepareStatement(insertAuthUserGroupSQL);
            preparedStatementInsert.setString(1, "ADMIN");
            preparedStatementInsert.setString(2, username);
            preparedStatementInsert.executeUpdate();

            conn.commit();

            // note that i'm leaving "date_created" out of this insert statement
            /*st.executeUpdate("INSERT INTO user (isacitve, username, password) "
                    +"VALUES (1,'"+username+"','"+passwordEncoder.encode(password)+"')");
            st.executeUpdate("INSERT INTO account (address, birthday, city, email, firstname," +
                    " lastname, phonenumber, username, zip)"
                    +"VALUES (address+','"+birthday+"','"+city+"','"+email+"','"+firstName+"','"+lastName+"','"+phoneNumber+"','"+username+"','"+zipCode+"')");
            st.executeUpdate("INSERT INTO auth_user_group (auth_group, username) "
                    +"VALUES ('ADMIN','"+username+"')");*/

            conn.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
            conn.rollback();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }
}
