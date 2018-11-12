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

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;


    public CourseServicesController(LessonRepository lessonRepository, CourseRepository courseRepository){
        super();
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    @PostMapping("/addCourseLessons")
    public ResponseEntity<Course> addCourseLessons(@RequestBody CourseModel model){
        String usernamesString = model.getStudentUsernames();
        ArrayList<Date> lessonDates = createLessonDates(model.getStartDate(), model.getWeekdays(), model.getNumberLessons(), model.getNumberLessonsADay());
        /* All added lessons will be initialized as active */
        boolean isSigned = false;
        List<Lesson> lessonList = lessonRepository.findAll();
        Collections.sort(lessonList, new SortByID());
        long lastEnteredLessonID;
        lastEnteredLessonID = lessonList.get(lessonList.size()).getId();

        for(int j = 0; j<lessonDates.size(); j++) {
            long lessonID = lastEnteredLessonID + 1 + j;
            Date lessonDate = lessonDates.get(j);
            Lesson lesson = new Lesson();
            lesson.setId(lessonID);
            lesson.setSigned(isSigned);
            lesson.setLessonDate(lessonDate);
            lesson.setLessonInstructor(model.getInstructorName());
            lesson.setLessonLocation(model.getLocation());
            lesson.setStudentList(model.getStudentUsernames());
            lesson.setCourseId(model.getCourseTick());
            Lesson saveLesson = this.lessonRepository.save(lesson);
        }
        // Hvordan fortsætter jeg herfra?
        Course course = this.courseRepository.save(model.translateModelToCourse()); // Her skal funktionaliteten være
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(course.getId()).toUri();
        return ResponseEntity.created(location).body(course);
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public void deleteCourse(@PathVariable Long id){
        Optional<Course> existing = this.courseRepository.findById(id);
        if(!existing.isPresent()){
            throw new CourseNotFoundException("Course not found with id: " + id);
        }
        this.courseRepository.deleteById(id);
    }

    /* Creates an ArrayList of dates with the dates a course should contain. The dates are set to start at startDate
and lessons will be distributed in the specified weekdays. Sunday is 0, monday is 1 and so on.
*/
    public ArrayList<Date> createLessonDates(Date startDate, ArrayList<Integer> weekdays, int numberLessonsToDistribute,
                                             int numberLessonsADay) {
        ArrayList<Date> lessonDates = new ArrayList<>();
        Date currentDayDate = startDate;
        int dayCount = 0;
        /* A lesson should minimum be 45 minutes according to law, and the two interviewed driving schools had a 45
           minute lesson duration. */
        int lessonDurationMinutes = 45;

        int oneMinuteInMilliseconds = 60000;
        int lessonDurationMilliseconds = oneMinuteInMilliseconds * lessonDurationMinutes;

        /* While all lessons has not yet been distributed */
        while (numberLessonsToDistribute > 0) {
            /* 86400000 * dayCount is not contained in a variable due to the possible of reaching an overflow of most
               data-types. Date is by default suitable to handle very large numbers. */
            currentDayDate = new Date(startDate.getTime() + 86400000 * dayCount);
            if(weekdays.contains(currentDayDate.getDay())) {
                /* If it is one of the weekdays specified in the weekdays array, add an number of lessons equal to
                 * number lessons a day. Also since it adds a several lessons in a loop it needs to check before each lesson
                 * is added if the necessary lessons have been distributed. */
                for(int g = 0; g<numberLessonsADay; g++){
                    if(numberLessonsToDistribute > 0) {
                        System.out.println(g);
                        lessonDates.add(new Date(currentDayDate.getTime() + lessonDurationMilliseconds * g));
                        numberLessonsToDistribute--;
                    }
                }
            }
            //System.out.println(dayCount);
            dayCount += 1;
        }
        return lessonDates;
    }



    public String saveStudentnamesAsString(ArrayList<String> studentList) {
        String combinedString = "";
        for (String student : studentList) {
            combinedString += student + ":";
        }
        return combinedString;
    }

    //Reformats the Java Date format to the mySQL datetime format and returns it in a String.
    //This functions works based on the Java Date format for generating a Date.
    //This means that this function counterworks the addition of +1900 in allocating a new Date.
    //Therefor any pre-formatted dates won't work with this function.
    private String reformatDate(Date date) {
        if(date == null) {
            try {
                throw new Exception("Invalid date.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String formattetDate = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate()
                + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
        return formattetDate;
    }
}