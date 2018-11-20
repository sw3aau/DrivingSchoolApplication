package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.service.LessonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class LogbookController {
    private final LessonService lessonService;

    public LogbookController(LessonService lessonService) {
        super();
        this.lessonService = lessonService;
    }

    /**
     * Get for the page.
     *
     * @param model
     * @return
     */
    @GetMapping(value = {"/logbook/student"})
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getStudentLogbookLessons(Model model) {
        List<Lesson> lessonList = this.lessonService.getAllLessons();
        //Creates a list, to store the user's lessons in the logbook
        List<Lesson> logbookLessonList = new ArrayList<>();

        //Fetches the username from the session
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        for(int i = 0; i < lessonList.size(); i++) {
            if(lessonList.get(i).getStudentList().contains(username)) {
                logbookLessonList.add(lessonList.get(i));
            }
        }

        //Models the lists as an attribute to the website
        model.addAttribute("logbookLessonList", logbookLessonList);
        return "logbook-student";
    }

    /**
     * Get for the page.
     *
     * @param model
     * @return
     */
    @GetMapping(value = {"/logbook/instructor"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getInstructorLogbookLessons(Model model) {
        List<Lesson> lessonList = this.lessonService.getAllLessons();
        //Creates a list, to store the user's lessons in the logbook
        List<Lesson> logbookLessonList = new ArrayList<>();

        //Fetches the username from the session
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        for(int i = 0; i < lessonList.size(); i++) {
            if(lessonList.get(i).getStudentList().contains(username)) {
                logbookLessonList.add(lessonList.get(i));
            }
        }

        //Models the lists as an attribute to the website
        model.addAttribute("logbookLessonList", logbookLessonList);
        return "logbook-instructor";
    }
}
