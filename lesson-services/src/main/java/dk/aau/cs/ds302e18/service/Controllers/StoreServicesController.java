package dk.aau.cs.ds302e18.service.Controllers;

import dk.aau.cs.ds302e18.service.Models.Store;
import dk.aau.cs.ds302e18.service.Models.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/storerequests")
public class StoreServicesController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreServicesController.class);

    private final StoreRepository storeRepository;

    public StoreServicesController(StoreRepository storeRepository)
    {
        super();
        this.storeRepository = storeRepository;
    }

    @GetMapping
    public List<Store> getAllStoreRequests()
    {
        System.out.println(this.storeRepository.findAll());
        return new ArrayList<>(this.storeRepository.findAll());
    }


}
