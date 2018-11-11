package dk.aau.cs.ds302e18.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/course")
public class CourseServiceController {

    private Connection conn;
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonServicesController.class);
    private final CourseRepository courseRepository;
    //private final LessonRepository lessonRepository;

    public CourseServiceController(/*LessonRepository lessonRepository,*/ CourseRepository courseRepository) {
        super();
        //this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.conn = new DBConnector().createConnectionObject();
    }

    @GetMapping
    public List<Course> getAllCourseRequests()
    {
        System.out.println(this.courseRepository.findAll());
        return new ArrayList<>(this.courseRepository.findAll());
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable Long id){
        Optional<Course> course = this.courseRepository.findById(id);
        if(course.isPresent()){
            return course.get();
        }
        throw new CourseNotFoundException("Course not found with id: " + id);
    }

    /* Post = responsible for posting new information directly after it has been created to the website, and create fitting
    links to the new information. */
    @PostMapping
    public ResponseEntity<Course> addCourse(@RequestBody CourseModel model){
        /* Translates the input entered in the add course menu into input that can be entered in the database. */
        Course course = this.courseRepository.save(model.translateModelToCourse());
        /* The new course will be placed in the current browser /id , with an id that matches the entered storeadmin ID. */
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(course.getId()).toUri();
        /* The connection to the new course is created. */
        return ResponseEntity.created(location).body(course);
    }



    /* Put = responsible for updating existing database entries*/
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteStore(@PathVariable Long id){
        Optional<Course> existing = this.courseRepository.findById(id);
        if(!existing.isPresent()){
            throw new CourseNotFoundException("Store not found with id: " + id);
        }
        this.courseRepository.deleteById(id);
    }
}
