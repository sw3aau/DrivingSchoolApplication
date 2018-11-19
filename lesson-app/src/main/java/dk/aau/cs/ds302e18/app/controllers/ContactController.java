package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.Notification;
import dk.aau.cs.ds302e18.app.domain.StoreModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping
public class ContactController {

    public ContactController() {
        super();

    }

    @GetMapping(value = "/contact")
    public String getContactPage() {
        return "contact-formular";
    }


    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public RedirectView acceptContactState(@RequestParam("firstName") String firstName, @RequestParam("email") String email,
                                       @RequestParam("message") String message, Model model,
                                       @ModelAttribute StoreModel storeModel) {
        String sendmessage = ("New Email from : " + firstName + " \n" + "Email :" + email + " \n" + "Message : " + message);
        System.out.println( sendmessage + email);
        new Notification(sendmessage, email);
        return new RedirectView("contact");
    }
}
