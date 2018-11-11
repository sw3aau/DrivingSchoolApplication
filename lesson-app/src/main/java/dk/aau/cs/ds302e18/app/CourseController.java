package dk.aau.cs.ds302e18.app;

import dk.aau.cs.ds302e18.app.domain.Course;
import dk.aau.cs.ds302e18.app.domain.CourseModel;
import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.domain.LessonModel;
import dk.aau.cs.ds302e18.app.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        super();
        this.courseService = courseService;
    }

    @GetMapping(value = "/course/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddLessonForm(Model model)
    {
        return "course-view";
    }

    @PostMapping(value = "/course")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCourse(HttpServletRequest request, Model model, @ModelAttribute CourseModel courseModel){
        /* The newly added course object is retrieved from the 8100 server.  */
        Course course = courseService.addCourse(courseModel);
        model.addAttribute("course", course);

        /* Cannot make .isEmpty work so skipped the exception part, see add lesson for details */
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/course/" + course.getCourseID());
    }

    @PostMapping(value = "/course/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateCourse(Model model, @PathVariable int id, @ModelAttribute CourseModel courseModel)
    {
        /* Returns an course that is read from the 8100 server through updateCourse. */
        Course course = this.courseService.updateCourse(id, courseModel);
        model.addAttribute("course", course);
        model.addAttribute("courseModel", new CourseModel());
        return "course-view";
    }
}
