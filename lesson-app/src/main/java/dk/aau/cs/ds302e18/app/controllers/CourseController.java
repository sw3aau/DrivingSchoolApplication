package dk.aau.cs.ds302e18.app.controllers;


import dk.aau.cs.ds302e18.app.SortByID;
import dk.aau.cs.ds302e18.app.domain.Course;
import dk.aau.cs.ds302e18.app.domain.CourseModel;
import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.domain.LessonModel;
import dk.aau.cs.ds302e18.app.service.CourseService;
import dk.aau.cs.ds302e18.app.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/")
public class CourseController {

    private final LessonService lessonService;
    private final CourseService courseService;


    public CourseController(LessonService lessonService, CourseService courseService) {
        super();
        this.lessonService = lessonService;
        this.courseService = courseService;
    }

    @GetMapping(value = "/course")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCourses(Model model) {
        /* Creates an list of courses from the return value of getAllLessons in LessonService(which is an function that gets courses
        from the 8100 server and makes them into lesson objects and returns them as an list) */
        List<Course> courses = this.courseService.getAllCourseRequests();
        model.addAttribute("courses", courses);
        return "courses-view";
    }

    @GetMapping(value = "/course/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddCourseForm(Model model) {
        return "course-view";
    }



    @GetMapping(value = "/course/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCourse(Model model, @PathVariable long id) {
        Course course = this.courseService.getCourseRequest(id);
        model.addAttribute("course", course);
        return "course-view";
    }

    /* HTML for updating an lesson */
    @PostMapping(value = "/course/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateCourse(Model model, @PathVariable long id, @ModelAttribute CourseModel courseModel) {
        /* Returns an course that is read from the 8100 server through updateLesson. */
        Course course = this.courseService.acceptCourseRequest(id, courseModel);
        model.addAttribute("course", course);
        model.addAttribute("courseModel", new CourseModel());
        return "course-view";
    }

    /* Get list of students from course database */
    /* Prepare lesson date, lesson ID, instructor, location, type. DONE */
    /* needs to sort lessons after ID, and pick the ID incremented by one. DONE */
    /* Still needs number of hours at a time and start date should be the exact time the first lesson starts */
    /* every lesson needs an associated courseID if there are any */
    @PostMapping(value = "/course")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView courseAddLessons(HttpServletRequest request, Model model, @ModelAttribute CourseModel courseModel) {
        ArrayList<Date> lessonDates = createLessonDates(courseModel.getStartDate(), courseModel.getWeekdays(), courseModel.getNumberLessons(), courseModel.getNumberLessonsADay());
        /* All added lessons will be initialized as active */
        boolean isSigned = false;
        List<Lesson> lessonList = lessonService.getAllLessons();

        for(int j = 0; j<lessonDates.size(); j++) {
            Date lessonDate = lessonDates.get(j);
            LessonModel lesson = new LessonModel();
            lesson.setSigned(isSigned);
            lesson.setLessonDate(lessonDate);
            lesson.setLessonInstructor(courseModel.getInstructorName());
            lesson.setLessonLocation(courseModel.getLocation());
            lesson.setStudentList(courseModel.getStudentUsernames());
            lesson.setCourseId(courseModel.getCourseTick());
            lesson.setLessonType(courseModel.getLessonType());

            lessonService.addLesson(lesson);
        }
        Course course = this.courseService.addCourseLessons(courseModel);
        /* Exceptions for when part of the model is empty */
        model.addAttribute("course", course);
        return new ModelAndView(/*"redirect:/lessons/" + lesson.getId() */);
    }

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
}
