package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.DBConnector;
import dk.aau.cs.ds302e18.app.RegisterUser;
import dk.aau.cs.ds302e18.app.Student;
import dk.aau.cs.ds302e18.app.auth.Account;
import dk.aau.cs.ds302e18.app.auth.AccountRespository;
import dk.aau.cs.ds302e18.app.auth.AuthGroup;
import dk.aau.cs.ds302e18.app.auth.AuthGroupRepository;
import dk.aau.cs.ds302e18.app.domain.StudentModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Controller responsible for handling actions towards the account
 */
@Controller
public class AccountController
{

    private final AccountRespository accountRespository;
    private final AuthGroupRepository authGroupRepository;

    public AccountController(AccountRespository accountRespository, AuthGroupRepository authGroupRepository) {
        this.accountRespository = accountRespository;
        this.authGroupRepository = authGroupRepository;
    }

    @GetMapping(value = "/manage")
    public String getManageAccount(Model model)
    {
        model.addAttribute("user", accountRespository.findByUsername(getAccountUsername()));
        model.addAttribute("userAuth",
                authGroupRepository.findByUsername(getAccountUsername()).get(0).getAuthGroup());
        return "manage-account";
    }

    @ModelAttribute("gravatar")
    public String gravatar() {

        //Models Gravatar
        System.out.println(accountRespository.findByUsername(getAccountUsername()).getEmail());
        String gravatar = ("http://0.gravatar.com/avatar/"+md5Hex(accountRespository.findByUsername(getAccountUsername()).getEmail()));
        return (gravatar);
    }

    public String getAccountUsername()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return username;
    }

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

}
