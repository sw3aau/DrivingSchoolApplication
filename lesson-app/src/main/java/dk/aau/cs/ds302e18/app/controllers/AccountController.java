package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.DBConnector;
import dk.aau.cs.ds302e18.app.auth.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Controller responsible for handling actions towards the account
 */
@Controller
public class AccountController
{

    private final AccountRespository accountRespository;
    private final AuthGroupRepository authGroupRepository;
    private final UserRepository userRepository;

    public AccountController(AccountRespository accountRespository, AuthGroupRepository authGroupRepository,
                             UserRepository userRepository) {
        this.accountRespository = accountRespository;
        this.authGroupRepository = authGroupRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/account/edit")
    public String getManageAccount(Model model)
    {
        model.addAttribute("user", accountRespository.findByUsername(getAccountUsername()));
        model.addAttribute("userAuth",
                authGroupRepository.findByUsername(getAccountUsername()).get(0).getAuthGroup());
        return "manage-account";
    }

    /**
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param email The email of the user
     * @param phoneNumber The phone number of the user
     * @param birthday The birthday of the user
     * @param address The address of the user
     * @param city The city of the user
     * @param zip The zip of the user
     * @return The site to be redirected to
     */
    @RequestMapping(value = "/account/edit/details", method = RequestMethod.POST)
    public RedirectView changeAccountDetails(@RequestParam("FirstName") String firstName,
                                             @RequestParam("LastName") String lastName,
                                             @RequestParam("Email") String email,
                                             @RequestParam("PhoneNumber") String phoneNumber,
                                             @RequestParam("Birthday") String birthday,
                                             @RequestParam("Address") String address,
                                             @RequestParam("City") String city,
                                             @RequestParam("Zip") int zip)
    {
        // Retrieve account from the repository
        Account account = accountRespository.findByUsername(getAccountUsername());

        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPhoneNumber(phoneNumber);
        account.setBirthday(birthday);
        account.setAddress(address);
        account.setCity(city);
        account.setZipCode(zip);
        this.accountRespository.save(account);
        return new RedirectView("redirect:/account/edit");
    }

    @RequestMapping(value = "/account/edit/password", method = RequestMethod.POST)
    public RedirectView changeAccountPassword(@RequestParam("Password") String password)
    {
        // Get current user by username
        User user = userRepository.findByUsername(getAccountUsername());

        // Initialize BCryptPasswordEncoder with strength 11
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11);

        // Encode and store the password in the variable
        String encodedPassword = passwordEncoder.encode(password);

        // Replace the user's password with the new encoded password
        user.setPassword(encodedPassword);

        // Save the user back into the repository
        this.userRepository.save(user);
        return new RedirectView("redirect:/account/edit");
    }

    @ModelAttribute("gravatar")
    public String gravatar() {

        //Models Gravatar
        System.out.println(accountRespository.findByUsername(getAccountUsername()).getEmail());
        return "http://0.gravatar.com/avatar/"+md5Hex(accountRespository.findByUsername(getAccountUsername()).getEmail());
    }

    public String getAccountUsername()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((UserDetails) principal).getUsername();
    }

    /**
     * @param response The response to be returned to the user as a file
     * @throws IOException If the MySQL errors
     */
    @RequestMapping(value = "/account/exportCalendar", method = RequestMethod.GET)
    @ResponseBody
    public void exportCalendar(HttpServletResponse response) throws IOException
    {
        // Create the csv file
        File file = new File("lesson-calendar-export.csv");

        // Create FileWriter object, it will not append to the file
        FileWriter fileWriter = new FileWriter(file,false);

        // Create CSV headers which Google Calendar accepts
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
