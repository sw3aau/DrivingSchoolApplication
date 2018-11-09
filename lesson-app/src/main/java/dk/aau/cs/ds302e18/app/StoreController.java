package dk.aau.cs.ds302e18.app;

import dk.aau.cs.ds302e18.app.domain.Store;
import dk.aau.cs.ds302e18.app.domain.StoreModel;
import dk.aau.cs.ds302e18.app.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping(value = "/storerequests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addStoreRequest(HttpServletRequest request, Model model, @ModelAttribute StoreModel storeModel)
    {
        Store store = this.storeService.addStoreRequest(storeModel);
        model.addAttribute("store", store);
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/store/" + store.getId());
    }

    @GetMapping(value = "/storerequests")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getStoreRequest(Model model)
    {
        List<Store> storeList = this.storeService.getAllStoreRequests();
        model.addAttribute("storerequests", storeList);
        return"store-request-view";
    }


}

