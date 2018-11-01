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
import java.util.Random;

@Controller
@RequestMapping("/")
public class LessonController
{

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

    @GetMapping(value="/canvas")
    public String getCanvasPage(){
        return "canvas";
    }

    @PostMapping(value="/canvas")
    public String postCanvasPage(@ModelAttribute CanvasModel canvasModel){
        System.out.println("test");
        System.out.println(canvasModel.getDataUrl());
        return "canvas";
    }

    @PostMapping(value="/test")
    public String postTestPage(@RequestBody CanvasModel canvasModel){
        Canvas.upload("p3-project","CoolSignature",canvasModel.getDataUrl());
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
        List<Lesson> lessons = this.lessonService.getAllLessons();
        model.addAttribute("lessons", lessons);
        return "lessons-view";
    }

    @GetMapping(value="/lessons/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAddLessonForm(Model model){
        return "lesson-view";
    }

    @PostMapping(value="/lessons")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addLesson(HttpServletRequest request, Model model, @ModelAttribute LessonModel lessonModel){
        Lesson lesson = this.lessonService.addLesson(lessonModel);
        model.addAttribute("lesson", lesson);
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/lessons/" + lesson.getId());
    }

    @GetMapping(value="/lessons/{id}")
        @PreAuthorize("hasRole('ROLE_USER')")
        public String getLesson(Model model, @PathVariable long id){
            Lesson lesson = this.lessonService.getLesson(id);
        model.addAttribute("lesson", lesson);
        return "lesson-view";
    }

    @PostMapping(value="/lessons/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateLesson(Model model, @PathVariable long id, @ModelAttribute LessonModel lessonModel){
        Lesson lesson = this.lessonService.updateLesson(id, lessonModel);
        model.addAttribute("lesson", lesson);
        model.addAttribute("lessonModel", new LessonModel());
        return "lesson-view";
    }

}
