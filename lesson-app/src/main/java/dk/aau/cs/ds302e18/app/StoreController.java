package dk.aau.cs.ds302e18.app;

import dk.aau.cs.ds302e18.app.auth.User;
import dk.aau.cs.ds302e18.app.domain.Store;
import dk.aau.cs.ds302e18.app.domain.StoreModel;
import dk.aau.cs.ds302e18.app.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class StoreController
{
    private final StoreService storeService;

    public StoreController(StoreService storeService)
    {
        super();
        this.storeService = storeService;
    }

    @GetMapping(value = "/storeadmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getStores(Model model)
    {
        /** Stores all the requests in the storeadmin ArrayList, and then iterates them through to the list ararylist to only
         * get the requets with the pending tag.*/
        List<Store> storeadmin = this.storeService.getAllStoreRequests();
        List<Store> list = new ArrayList<>();
        for (Store store : storeadmin) if (store.getState() == 0) list.add(store);
        model.addAttribute("storeadmin", list);
        return "storeadmin-view";
    }

    @GetMapping(value = "/storeadmin/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddStoreForm(Model model)
    {
        return "store-view";
    }

    /* Posts a newly added store in the storeadmin list on the website */
    @PostMapping(value = "/storeadmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addStore(HttpServletRequest request, Model model, @ModelAttribute StoreModel storeModel)
    {
        /* The newly added store object is retrieved from the 8100 server.  */
        Store store = this.storeService.addStoreRequest(storeModel);
        model.addAttribute("store", store);
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/storeadmin/" + store.getId());
    }

    @GetMapping(value = "/storeadmin/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getStore(Model model, @PathVariable long id)
    {
        Store store = this.storeService.getStoreRequest(id);
        model.addAttribute("store", store);
        return "store-view";
    }

    /* HTML for updating an storeadmin */
    @PostMapping(value = "/storeadmin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateLesson(Model model, @PathVariable long id, @ModelAttribute StoreModel storeModel)
    {
        /* Returns an lesson that is read from the 8100 server through updateLesson. */
        Store store = this.storeService.acceptStoreRequest(id, storeModel);
        model.addAttribute("store", store);
        model.addAttribute("storeModel", new StoreModel());
        return "store-view";
    }
    @RequestMapping(value="/accept", method=RequestMethod.POST)
    public RedirectView acceptAppState(@RequestParam("appId") long appId, @RequestParam("courseIdAccept") long courseId,
                                       @RequestParam("studentUsernameAccept") String studentUsername, Model model,
                                       @ModelAttribute StoreModel storeModel) {
        Byte b = 1;
        storeModel.setState(b);
        storeModel.setCourseId(courseId);
        storeModel.setStudentUsername(studentUsername);
        Store store = this.storeService.acceptStoreRequest(appId, storeModel);
        model.addAttribute("store", store);
        model.addAttribute("storeModel", new StoreModel());
        return new RedirectView("storeadmin");
    }

    @RequestMapping(value="/deny", method=RequestMethod.POST)
    public RedirectView denyAppState(@RequestParam("appIdDeny") long appId, @RequestParam("courseIdDeny") long courseId,
                                     @RequestParam("studentUsernameDeny") String studentUsername, Model model,
                                     @ModelAttribute StoreModel storeModel) {
        Byte b = 2;
        storeModel.setState(b);
        storeModel.setCourseId(courseId);
        storeModel.setStudentUsername(studentUsername);
        Store store = this.storeService.acceptStoreRequest(appId, storeModel);
        model.addAttribute("store", store);
        model.addAttribute("storeModel", new StoreModel());
        return new RedirectView("storeadmin");
    }
/*
    @RequestMapping(value="/apply", method=RequestMethod.POST)
    public RedirectView createAppState(@RequestParam("applyCourseId") long courseId,
                                     @RequestParam("applyStudentUsername") String studentUsername, Model model,
                                     @ModelAttribute StoreModel storeModel)
    {
        Byte b = 0;
        storeModel.setState(b);
        storeModel.setCourseId(courseId);
        storeModel.setStudentUsername(studentUsername);
        System.out.println(storeModel.toString());

    //    Store store = this.storeService.acceptStoreRequest(appId, storeModel);
      //  model.addAttribute("store", store);
        //model.addAttribute("storeModel", new StoreModel());
        return new RedirectView("storeadmin");
    }
    */

    @PostMapping(value = "/apply")
    public String createAppState(HttpServletRequest request, Model model, @ModelAttribute StoreModel storeModel)
    {
        // Set the state of a lesson to Pending
        byte b = 0;
        storeModel.setState(b);

        //Fetches the username from the session
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();

        storeModel.setStudentUsername(username);

        Store store = this.storeService.addStoreRequest(storeModel);
        model.addAttribute("store", store);
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return "index";
    }
}

