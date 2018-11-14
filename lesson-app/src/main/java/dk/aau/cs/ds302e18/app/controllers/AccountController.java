package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.DBConnector;
import dk.aau.cs.ds302e18.app.ModifyUser;
import dk.aau.cs.ds302e18.app.Student;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Controller responsible for handling actions towards the account
 */
@Controller
public class AccountController
{
    private Connection conn;

    public AccountController() {this.conn = new DBConnector().createConnectionObject();}
    
    @RequestMapping(value = "/account/exportCalendar", method = RequestMethod.GET)
    @ResponseBody
    public void exportCalendar(HttpServletResponse response) throws IOException
    {
        // Create the csv file
        File file = new File("lesson-calendar-export.csv");

        // Create FileWriter object, it will not append to the file
        FileWriter fileWriter = new FileWriter(file,false);

        // Create headers which Google Calendar accepts
        fileWriter.write("Subject, Start date, Start time\n");

        // TODO: Potentially shift this towards "services" instead of directly on controller
        Connection connection = new DBConnector().createConnectionObject();

        // Only the instructor and lesson date is necessary
        String query = "SELECT instructor,lesson_date FROM lesson";

        try
        {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            // If query failed, simply return
            if (rs == null)
                return;

            // Iterate over all results
            while (rs.next())
            {
                String username = rs.getString("instructor");

                // TODO: Make instructor name dynamic, will wait until role permissions are correctly in place
                if (!username.equals("Instructor Samuel"))
                    continue;

                fileWriter.write("Lesson, " + rs.getDate("lesson_date").toString() + ", " + rs.getTime("lesson_date") + "\n");
            }
        }
        catch (Exception e)
        {
            e.getCause();
        }

        fileWriter.close();

        response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() +"\""));
        response.setContentLength((int)file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
    
    
    public void editAccount(String address, String birthday, String email, String firstname, String city,
                            String lastname, int phonenumber, String username, int zip ) {
        try {
            Statement st = conn.createStatement();
            st.executeUpdate("UPDATE `account` SET `address` = '"+address+"',  `birthday` = '"+birthday+"',   " +
                    "`email` = '"+email+"', `firstname` = '"+firstname+"', `lastname`='"+lastname+"', `city`='"+city+"', " +
                    " `phonenumber` = "+phonenumber+", `zip`="+zip+" WHERE `username` = '"+username+"'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @GetMapping(value = "/modifyAccount")
    public String getModifyPage()
    {
        return "modify-account";
    }

    @GetMapping(value = "/contact")
    public String getContactPage()
    {
        return "contact-formular";
    }

    @PostMapping(value = "/modifyAccount")
    public String postModifyPage(@ModelAttribute Student student){
        System.out.println(student.toString());
        new ModifyUser(student.getUsername(), student.getPassword(), student.getFirstName(), student.getLastName(),
                student.getPhonenumber(), student.getEmail(), student.getBirthdate(), student.getAddress(),
                student.getZipCode(), student.getCity());
        return "modify-account";
    }

    public void getUsername() {
        getUsername(); {this.conn = new DBConnector().createConnectionObject();}

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT`address`, `birthday`, `city`, `email`, `firstname`," +
                    " `lastname`, `phonenumber`, `username`, `zip` FROM `account` WHERE `username` = Sembrik");
            rs.next();
            String accountAddress = rs.getString("address");
            String accountBirthday = rs.getString("birthday");
            String accountCity = rs.getString("city");
            String accountFirstname = rs.getString("firstname");
            String accountLastname = rs.getString("lastname");
            String accountPhonenumber = rs.getString("phonenumber");
            String accountZip = rs.getString("zip");
            String accountUserName = rs.getString("username");

            conn.close();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}