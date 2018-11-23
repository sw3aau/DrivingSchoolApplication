package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.domain.Logbook;
import dk.aau.cs.ds302e18.app.service.LessonService;
import dk.aau.cs.ds302e18.app.service.LogbookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class LogbookController {
    private final LessonService lessonService;
    private final LogbookService logbookService;

    public LogbookController(LessonService lessonService, LogbookService logbookService) {
        super();
        this.lessonService = lessonService;
        this.logbookService = logbookService;
    }

    /**
     * Retrieves all logbooks and selects the student's logbooks and models them for the html site
     * @param model
     * @return
     */
    @GetMapping(value = {"/logbook/student"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getAllStudentLogbooks(Model model) {
        List<Logbook> logbookList = this.logbookService.getAllLogbooks();
        List<Logbook> studentLogbookList = new ArrayList<>();

        //Fetches the username from the session
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        for (Logbook logbook : logbookList) {
            if (logbook.getStudent().equals(username)) {
                studentLogbookList.add(logbook);
            }
        }

        //Models the lists as an attribute to the website
        model.addAttribute("studentLogbookList", studentLogbookList);
        model.addAttribute("studentUsername", username);
        return "logbook-student";
    }

    
    @GetMapping(value = {"/logbook/instructor"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getInstructorLogbookLessons(Model model) {
        List<Lesson> lessonList = this.lessonService.getAllLessons();
        //Creates a list, to store the user's lessons in the logbook
        List<Lesson> logbookLessonList = new ArrayList<>();

        //Fetches the username from the session
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        for (int i = 0; i < lessonList.size(); i++) {
            if (lessonList.get(i).getStudentList().contains(username)) {
                logbookLessonList.add(lessonList.get(i));
            }
        }

        //Models the lists as an attribute to the website
        model.addAttribute("logbookLessonList", logbookLessonList);
        return "logbook-instructor";
    }

    /**
     * Retrieves details about a student's specific logbook and models it for the html site
     * @param model
     * @param id
     * @return
     */
    @GetMapping(value = "/logbook/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getStudentLogbook(Model model, @PathVariable long id) {
        Logbook logbook = this.logbookService.getLogbook(id);
        System.out.println("loading logbooksite");
        List<Lesson> logbookLessonList = new ArrayList<>();

        //Fetches the username from the session
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        if(!logbook.getStudent().equals(username)) {
            return "error";
        }
        else {
            String[] logbookLessonIdList = logbook.getLessonList().split(",");
            for (String string : logbookLessonIdList) {
                Lesson lesson = lessonService.getLesson(Long.parseLong(string));
                logbookLessonList.add(lesson);
                System.out.println(string);
            }

            model.addAttribute("logbookLessonList", logbookLessonList);
            model.addAttribute("logbookId", id);
            return "logbook-view";
        }
    }
}
