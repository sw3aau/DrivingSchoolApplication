package dk.aau.cs.ds302e18.website.controllers;

import dk.aau.cs.ds302e18.shared.model.Store;
import dk.aau.cs.ds302e18.shared.model.StoreModel;
import dk.aau.cs.ds302e18.website.service.StoreService;
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

    /**
     * Stores all the requests in the storeadmin ArrayList, and then iterates them through to the list ararylist to only
     * get the requets with the pending tag (0), and present it for the admins.
     * @param model
     * @return
     */
    @GetMapping(value = "/storeadmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getStores(Model model)
    {
        // Fetching all requests in a list
        List<Store> storeadmin = this.storeService.getAllStoreRequests();
        // Creating a new list, to store the filtered requests
        List<Store> list = new ArrayList<>();
        // Iterates through all requests, adding the ones with state (0) into the filtered request list.
        for (Store store : storeadmin) if (store.getState() == 0) list.add(store);
        model.addAttribute("storeadmin", list);
        return "storeadmin-view";
    }

    @GetMapping(value = "/store")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getStore(Model model)
    {
        // Fetching all requests in a list
        List<Store> storeadmin = this.storeService.getAllStoreRequests();
        // Creating a new list, to store the filtered requests
        List<Store> list = new ArrayList<>();
        // Iterates through all requests, adding the ones with state (0) into the filtered request list.
        for (Store store : storeadmin) if (store.getState() == 0) list.add(store);
        model.addAttribute("store", list);
        return "store-page";
    }

    /**
     * Get for the page.
     * @param model
     * @return
     */
    @GetMapping(value = "/storeadmin/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddStoreForm(Model model)
    {
        return "store-view";
    }


    /**
     * Posts a newly added store in the requests list on the website
     * @param request
     * @param model
     * @param storeModel
     * @return
     */
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

    /**
     * Used for admins to hard change values in each request - view the information in each request
     * @param model
     * @param id
     * @return
     */
    @GetMapping(value = "/storeadmin/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getStore(Model model, @PathVariable long id)
    {
        Store store = this.storeService.getStoreRequest(id);
        model.addAttribute("store", store);
        return "store-view";
    }

    /**
     * Used for admins to hard change values in each request - updates the request in the databasse with the id.
     * @param model
     * @param id
     * @param storeModel
     * @return
     */
    @PostMapping(value = "/storeadmin/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateRequest(Model model, @PathVariable long id, @ModelAttribute StoreModel storeModel)
    {
        /* Returns an lesson that is read from the 8100 server through updateLesson. */
        Store store = this.storeService.acceptStoreRequest(id, storeModel);
        model.addAttribute("store", store);
        model.addAttribute("storeModel", new StoreModel());
        return "store-view";
    }

    /**
     * Accept Application is a RequestMapping from the accept button, taking the request id, and setting the state to 1,
     * which is accepted.
     *
     * Pending ID is 0.
     *
     * @param appId
     * @param courseId
     * @param studentUsername
     * @param model
     * @param storeModel
     * @return RedirectView (storeadmin)
     */
    @RequestMapping(value="/accept", method=RequestMethod.POST)
    public RedirectView acceptAppState(@RequestParam("appId") long appId, @RequestParam("courseIdAccept") long courseId,
                                       @RequestParam("studentUsernameAccept") String studentUsername, Model model,
                                       @ModelAttribute StoreModel storeModel) {
        // Predefining byte to state 1 (accepted application)
        Byte b = 1;

        // Setting the application state, course id and studentname from the request into the update model.
        storeModel.setState(b);
        storeModel.setCourseId(courseId);
        storeModel.setStudentUsername(studentUsername);

        // Creating the storemodel with the set values above, and updaing it.
        Store store = this.storeService.acceptStoreRequest(appId, storeModel);
        model.addAttribute("store", store);
        model.addAttribute("storeModel", new StoreModel());
        return new RedirectView("storeadmin");
    }

    /**
     * Deny Application is a RequestMapping from the deny button, taking the request id, and setting the state to 2,
     * which is denied.
     * @param appId
     * @param courseId
     * @param studentUsername
     * @param model
     * @param storeModel
     * @return RedirectView (storeadmin)
     */
    @RequestMapping(value="/deny", method=RequestMethod.POST)
    public RedirectView denyAppState(@RequestParam("appIdDeny") long appId, @RequestParam("courseIdDeny") long courseId,
                                     @RequestParam("studentUsernameDeny") String studentUsername, Model model,
                                     @ModelAttribute StoreModel storeModel) {
        // Predefining byte to state 2 (denied application)
        Byte b = 2;

        // Setting the application state, course id and studentname from the request into the update model.
        storeModel.setState(b);
        storeModel.setCourseId(courseId);
        storeModel.setStudentUsername(studentUsername);

        // Creating the storemodel with the set values above, and updaing it.
        Store store = this.storeService.acceptStoreRequest(appId, storeModel);
        model.addAttribute("store", store);
        model.addAttribute("storeModel", new StoreModel());
        return new RedirectView("storeadmin");
    }

    /**
     * Apply Post Mapping, for every time a student want to apply for a course, they post a request, where it checks
     * the id of the course, and post the request for the instructor.
     * @param request
     * @param model
     * @param storeModel
     * @return index page
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

        // Setting the username of the student who applied
        storeModel.setStudentUsername(username);

        // Creating the store mode, to be sent to the rest server
        Store store = this.storeService.addStoreRequest(storeModel);
        model.addAttribute("store", store);
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        // Returning index after you've applied
        return "index";
    }
}

