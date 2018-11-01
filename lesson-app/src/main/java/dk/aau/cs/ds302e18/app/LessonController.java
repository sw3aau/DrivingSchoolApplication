package dk.aau.cs.ds302e18.app;

import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.domain.LessonModel;
import dk.aau.cs.ds302e18.app.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/")
public class LessonController
{
    /* Mapping is what allows the java objects to be included and used by the html files.
       An object is available to the html file after it has been added as an attribute with the addAttribute method.
       What is returned is the html file it interacts with.
       The value assigned at the @GetMapping(value = ?) will be the url that redirects to that part of the website.
       @PreAuthorize defines what role is necessary to access the url. */

    private final LessonService lessonService;

    public LessonController(LessonService lessonService){
        super();
        this.lessonService = lessonService;
    }

    @GetMapping(value={"/", "/index"})
    public String getHomePage(Model model){
        model.addAttribute("penis", "FUk");
        return "index";
    }

    @GetMapping(value="/login")
    public String getLoginPage(Model model){
        return "login";
    }

    @GetMapping(value="/register")
    public String getRegisterPage(){
        return "register-account";
    }

    @PostMapping(value="/register")
    public String getRegisterPage(@ModelAttribute Student student){
        System.out.println(student.toString());
        return "register-account";
    }

    @GetMapping(value="/logout-success")
    public String getLogoutPage(Model model){
        return "logout";
    }

    @GetMapping(value="/lessons")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getLessons(Model model){
        /* Creates an list of lessons from the return value of getAllLessons in LessonService(which is an function that gets lessons
        from the 8100 server and makes them into lesson objects and returns them as an list) */
        List<Lesson> lessons = this.lessonService.getAllLessons();
        model.addAttribute("lessons", lessons);
        return "lessons-view";
    }

    @GetMapping(value="/lessons/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddLessonForm(Model model){
        return "lesson-view";
    }

    /* Posts a newly added lesson in the lessons list on the website */
    @PostMapping(value="/lessons")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addLesson(HttpServletRequest request, Model model, @ModelAttribute LessonModel lessonModel){
        /* The newly added lesson object is retrieved from the 8100 server.  */
        Lesson lesson = this.lessonService.addLesson(lessonModel);
        model.addAttribute("lesson", lesson);
        /* RESPONSE_STATUS_ATTRIBUTE checks that the 8100 connection is ready and HttpStatus.TEMPORARY_REDIRECT is a
           server status send to the put method to signal that the resource has been sent. */
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/lessons/" + lesson.getId());
    }

    /* The {id} means that the mapping html should hold for every url with /lessons/x no matter the ID. */
    @GetMapping(value="/lessons/{id}")
        @PreAuthorize("hasRole('ROLE_USER')")
        public String getLesson(Model model, @PathVariable long id){
            Lesson lesson = this.lessonService.getLesson(id);
        model.addAttribute("lesson", lesson);
        return "lesson-view";
    }

    /* HTML for updating an lesson */
    @PostMapping(value="/lessons/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateLesson(Model model, @PathVariable long id, @ModelAttribute LessonModel lessonModel){
        /* Returns an lesson that is read from the 8100 server through updateLesson. */
        Lesson lesson = this.lessonService.updateLesson(id, lessonModel);
        model.addAttribute("lesson", lesson);
        model.addAttribute("lessonModel", new LessonModel());
        return "lesson-view";
    }

}
