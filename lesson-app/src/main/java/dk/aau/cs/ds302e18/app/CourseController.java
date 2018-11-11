package dk.aau.cs.ds302e18.app;


import dk.aau.cs.ds302e18.app.domain.Course;
import dk.aau.cs.ds302e18.app.domain.CourseModel;
import dk.aau.cs.ds302e18.app.service.CourseService;
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
@RequestMapping("/")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        super();
        this.courseService = courseService;
    }

    @GetMapping(value = "/course")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCourses(Model model)
    {
        /* Creates an list of courses from the return value of getAllLessons in LessonService(which is an function that gets courses
        from the 8100 server and makes them into lesson objects and returns them as an list) */
        List<Course> courses = this.courseService.getAllCourseRequests();
        model.addAttribute("courses", courses);
        return "courses-view";
    }

    @GetMapping(value = "/course/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddCourseForm(Model model)
    {
        return "course-view";
    }

    /* Posts a newly added lesson in the lessons list on the website */
    @PostMapping(value = "/course")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCourse(HttpServletRequest request, Model model, @ModelAttribute CourseModel courseModel)
    {
        /* The newly added course object is retrieved from the 8100 server.  */
        Course course = this.courseService.addCourseRequest(courseModel);
        if (course.getStudentUsernames().isEmpty() | course.getCourseID() == 0)
        {
            throw new RuntimeException();
        }
        model.addAttribute("course", course);

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/course/" + course.getCourseID());
    }

    @GetMapping(value = "/course/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCourse(Model model, @PathVariable long id)
    {
        Course course = this.courseService.getCourseRequest(id);
        model.addAttribute("course", course);
        return "course-view";
    }

    /* HTML for updating an lesson */
    @PostMapping(value = "/course/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateCourse(Model model, @PathVariable long id, @ModelAttribute CourseModel courseModel)
    {
        /* Returns an course that is read from the 8100 server through updateLesson. */
        Course course = this.courseService.acceptCourseRequest(id, courseModel);
        model.addAttribute("course", course);
        model.addAttribute("courseModel", new CourseModel());
        //if (course.getLessonType() != 1 || course.getLessonType() != 2)
        //{
        //    throw new RuntimeException();
        //}
        return "course-view";
    }
}
