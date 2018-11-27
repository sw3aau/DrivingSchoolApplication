package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.auth.Account;
import dk.aau.cs.ds302e18.app.auth.AccountRespository;
import dk.aau.cs.ds302e18.app.auth.AuthGroupRepository;
import dk.aau.cs.ds302e18.app.auth.UserRepository;
import dk.aau.cs.ds302e18.app.domain.AccountViewModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Controller
public class AdminController
{

    private final AccountRespository accountRespository;
    private final AuthGroupRepository authGroupRepository;
    private final UserRepository userRepository;

    public AdminController(AccountRespository accountRespository, AuthGroupRepository authGroupRepository,
                             UserRepository userRepository) {
        this.accountRespository = accountRespository;
        this.authGroupRepository = authGroupRepository;
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/admin")
    public String getAdminPage(Model model)
    {
        ArrayList<AccountViewModel> accountViewModelList = new ArrayList<>();

        for (long i = 1; i < this.userRepository.findAll().size(); i++)
        {
            AccountViewModel accountViewModel = new AccountViewModel();
            accountViewModel.setUsername(this.userRepository.getOne(i).getUsername());
            accountViewModel.setFirstName(this.accountRespository.getOne(i).getFirstName());
            accountViewModel.setLastName(this.accountRespository.getOne(i).getLastName());
            accountViewModel.setAuthGroup(this.authGroupRepository.getOne(i).getAuthGroup());
            accountViewModelList.add(accountViewModel);
            System.out.println(accountViewModel.toString());
        }

        model.addAttribute("GetAllAccounts", accountViewModelList);
        System.out.println(accountViewModelList);
        return "admin";
    }

    @GetMapping(value = "/admin/{username}")
    public String getAdminPageForUser(Model model, @PathVariable String username)
    {
        model.addAttribute("user", accountRespository.findByUsername(username));
        model.addAttribute("userName", username);
        System.out.println(username);
        return "admin-edituser";
    }


    @RequestMapping(value = "/adminChangeDetails", method = RequestMethod.POST)
    public RedirectView changeAccountDetailsAdmin(@RequestParam("Username") String username,
                                                  @RequestParam("FirstName") String firstName,
                                             @RequestParam("LastName") String lastName,
                                             @RequestParam("Email") String email,
                                             @RequestParam("PhoneNumber") String phoneNumber,
                                             @RequestParam("Birthday") String birthday,
                                             @RequestParam("Address") String address,
                                             @RequestParam("City") String city,
                                             @RequestParam("Zip") int zip)
    {
        Account account = new Account();
        account.setUsername(username);
        account.setId(accountRespository.findByUsername(username).getId());
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPhoneNumber(phoneNumber);
        account.setBirthday(birthday);
        account.setAddress(address);
        account.setCity(city);
        account.setZipCode(zip);
        this.accountRespository.save(account);
        return new RedirectView("admin");
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

}
