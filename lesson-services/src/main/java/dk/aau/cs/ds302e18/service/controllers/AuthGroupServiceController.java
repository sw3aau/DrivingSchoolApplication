package dk.aau.cs.ds302e18.service.controllers;

import dk.aau.cs.ds302e18.service.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/authGroup")
public class AuthGroupServiceController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthGroupServiceController.class);

    private final AuthGroupRepository authGroupRepository;


    public AuthGroupServiceController(AuthGroupRepository authGroupRepository){
        super();
        this.authGroupRepository = authGroupRepository;
    }

    /* Returns all the courses in the database in a list */
    @GetMapping
    public List<AuthGroup> getAllAccounts(){
        return new ArrayList<>(this.authGroupRepository.findAll());
    }

    /* Get = responsible for retrieving information only */
    @GetMapping("/{id}")
    public AuthGroup getAccount(@PathVariable Long id){
        Optional<AuthGroup> account = this.authGroupRepository.findById(id);
        if(account.isPresent()){
            return account.get();
        }
        throw new CourseNotFoundException("Course not found with id: " + id);
    }

    /*
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseModel model){
        Course course = this.authGroupRepository.save(model.translateModelToCourse());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(course.getId()).toUri();
        return ResponseEntity.created(location).body(course);
    }
    */




/*
    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody CourseModel model){
        // Throw an error if the selected account do not exist.
        Optional<Account> existing = this.authGroupRepository.findById(id);
        if(!existing.isPresent()){
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        // Translates input from the interface into an account object
        Account account = model.translateModelToCourse();
        //  Uses the ID the account already had to save the account
        account.setId(id);
        return this.authGroupRepository.save(account);
    }
    */
}

