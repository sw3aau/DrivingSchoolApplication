package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.domain.LessonModel;
import dk.aau.cs.ds302e18.app.domain.Store;
import dk.aau.cs.ds302e18.app.domain.StoreModel;
import dk.aau.cs.ds302e18.app.service.LessonService;
import dk.aau.cs.ds302e18.app.service.StoreService;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping
public class IndexController {
    private final LessonService lessonService;

    public IndexController(LessonService lessonService) {
        super();
        this.lessonService = lessonService;
    }


    /**
     * Get for the page.
     *
     * @param model
     * @return
     */
    @GetMapping(value = {"/", "/index"})
    @PreAuthorize("isAuthenticated()")
    public String getTodaysLesson(Model model) {
        List<Lesson> lessonList = this.lessonService.getAllLessons();
        //Creating two new list, to store the specific user's lessons, one for today's lessons and one for upcoming lessons
        List<Lesson> todaysLessonList = new ArrayList<>();
        List<Lesson> upcomingLessonList = new ArrayList<>();

        //Creates new date for comparison for today's lessons
        Date currDate = new Date();

        //Fetches the username from the session
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        /**
         * If there is more then 7 lessons in the database, it limits the listed lesson to 7,
         * and only iterates through 6
         * */
        if (lessonList.size()>=7){
        //Iterates through all lessons, adding the ones with today's date to todaysLessonList
        for (int i = 0; i <= 6 ;i++) {
            if (lessonList.get(i).getLessonDate() != null &&
                    lessonList.get(i).getStudentList().contains(username) &&
                    lessonList.get(i).getLessonDate().getYear() == currDate.getYear() &&
                    lessonList.get(i).getLessonDate().getMonth() == currDate.getMonth() &&
                    lessonList.get(i).getLessonDate().getDate() == currDate.getDate()) {
                todaysLessonList.add(lessonList.get(i));
            }
        }

        //Iterates through all lessons, adding the upcoming lessons to upcomingLessonList
        for (int i = 0; i <= 6 ;i++) {
            if (lessonList.get(i).getLessonDate() != null && lessonList.get(i).getStudentList().contains(username)) {
                upcomingLessonList.add(lessonList.get(i));
            }
        }
        }

        /**
         * If there is less than 6 lessons in the database, no limiter is needed.
         * */
        if (lessonList.size()<6){
            for (Lesson lesson: lessonList)
            {
                if (lesson.getLessonDate() != null &&
                        lesson.getStudentList().contains(username) &&
                        lesson.getLessonDate().getYear() == currDate.getYear() &&
                        lesson.getLessonDate().getMonth() == currDate.getMonth() &&
                        lesson.getLessonDate().getDate() == currDate.getDate()) {
                    todaysLessonList.add(lesson);
                }

                if (lesson.getLessonDate() != null && lesson.getStudentList().contains(username)) {
                    upcomingLessonList.add(lesson);
                }
            }
        }

        //Models the lists as an attribute to the website
        model.addAttribute("todayLessonList", todaysLessonList);
        model.addAttribute("upcomingLessonList", upcomingLessonList);
        return "index";
    }


}