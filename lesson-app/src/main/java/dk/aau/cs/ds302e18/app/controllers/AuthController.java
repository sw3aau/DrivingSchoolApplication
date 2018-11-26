package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.RegisterUser;
import dk.aau.cs.ds302e18.app.Student;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLException;

/**
 * Controller responsible for handling actions towards the account
 */
@Controller
public class AuthController
{

    @GetMapping(value = "/logout-success")
    public String getLogoutPage(Model model)
    {
        return "logout";
    }

    @GetMapping(value = "/register")
    public String getRegisterPage()
    {
        return "register-account";
    }

    @PostMapping(value = "/register")
    public String getRegisterPage(@ModelAttribute Student student)
    {
        System.out.println(student.toString());
        try
        {
            new RegisterUser(student.getUsername(), student.getPassword(), student.getFirstName(), student.getLastName(),
                    student.getPhonenumber(), student.getEmail(), student.getBirthDay(), student.getAddress(),
                    student.getZipCode(), student.getCity());
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return "login";
    }

    @GetMapping(value = "/login")
    public String getLoginPage(Model model)
    {
        return "login";
    }


    public String getAccountUsername()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return username;
    }


}
