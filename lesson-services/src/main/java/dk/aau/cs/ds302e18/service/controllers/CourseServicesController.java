package dk.aau.cs.ds302e18.service.controllers;

import dk.aau.cs.ds302e18.service.SortByID;
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
@RequestMapping("/course")
public class CourseServicesController
{

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseServicesController.class);

    private final CourseRepository courseRepository;


    public CourseServicesController(CourseRepository courseRepository){
        super();
        this.courseRepository = courseRepository;
    }

    /* Returns all the courses in the database in a list */
    @GetMapping
    public List<Course> getAllCourses(){
        return new ArrayList<>(this.courseRepository.findAll());
    }

    /* Get = responsible for retrieving information only */
    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id){
        Optional<Course> course = this.courseRepository.findById(id);
        if(course.isPresent()){
            return course.get();
        }
        throw new CourseNotFoundException("Course not found with id: " + id);
    }

    /*
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseModel model){
        Course course = this.courseRepository.save(model.translateModelToCourse());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(course.getId()).toUri();
        return ResponseEntity.created(location).body(course);
    }
    */

    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseModel model){
        Course course = this.courseRepository.save(model.translateModelToCourse());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(course.getId()).toUri();
        return ResponseEntity.created(location).body(course);
    }





    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id, @RequestBody CourseModel model){
        /* Throw an error if the selected course do not exist. */
        Optional<Course> existing = this.courseRepository.findById(id);
        if(!existing.isPresent()){
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        /* Translates input from the interface into an course object */
        Course course = model.translateModelToCourse();
        /* Uses the ID the course already had to save the course */
        course.setId(id);
        return this.courseRepository.save(course);
    }



    /* NOT IMPLEMENTED: Delete = responsible for deleting database entries. */
    @RequestMapping(value="/deleteCourse", method=RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteCourse(/*@PathVariable Long id*/@ModelAttribute CourseModel courseModel){
        long id = (long)courseModel.getCourseTick();
        Optional<Course> existing = this.courseRepository.findById(id);
        System.out.println("Course Found with ID: " + courseModel.getCourseTick());
        if(!existing.isPresent()){
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        this.courseRepository.deleteById(id);
    }
}