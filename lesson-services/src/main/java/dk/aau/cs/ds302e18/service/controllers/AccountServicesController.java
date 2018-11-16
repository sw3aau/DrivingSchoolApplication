package dk.aau.cs.ds302e18.service.controllers;

import dk.aau.cs.ds302e18.service.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/account")
public class AccountServicesController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServicesController.class);

    private final AccountRespository accountRespository;


    public AccountServicesController(AccountRespository accountRespository){
        super();
        this.accountRespository = accountRespository;
    }

    /* Returns all the courses in the database in a list */
    @GetMapping
    public List<Account> getAllAccounts(){
        return new ArrayList<>(this.accountRespository.findAll());
    }

    /* Get = responsible for retrieving information only */
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id){
        Optional<Account> account = this.accountRespository.findById(id);
        if(account.isPresent()){
            return account.get();
        }
        throw new CourseNotFoundException("Course not found with id: " + id);
    }

    /*
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseModel model){
        Course course = this.accountRespository.save(model.translateModelToCourse());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(course.getId()).toUri();
        return ResponseEntity.created(location).body(course);
    }
    */




/*
    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody CourseModel model){
        // Throw an error if the selected account do not exist.
        Optional<Account> existing = this.accountRespository.findById(id);
        if(!existing.isPresent()){
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        // Translates input from the interface into an account object
        Account account = model.translateModelToCourse();
        //  Uses the ID the account already had to save the account
        account.setId(id);
        return this.accountRespository.save(account);
    }
    */
}
