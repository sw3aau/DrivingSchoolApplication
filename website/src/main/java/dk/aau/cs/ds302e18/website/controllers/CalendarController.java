package dk.aau.cs.ds302e18.website.controllers;

import dk.aau.cs.ds302e18.website.domain.CalendarViewModel;
import dk.aau.cs.ds302e18.website.domain.Lesson;
import dk.aau.cs.ds302e18.website.domain.LessonType;
import dk.aau.cs.ds302e18.website.service.LessonService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CalendarController
{
    private final LessonService lessonService;

    public CalendarController(LessonService lessonService)
    {
        super();
        this.lessonService = lessonService;
    }

    @GetMapping(value = "/calendar")
    public String getCalendar()
    {
        return "calendar";
    }

    @RequestMapping("/calendar/getData")
    public @ResponseBody
    ArrayList<CalendarViewModel> getCalendarData()
    {

        // Get the Spring Security user details of the currently logged in user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Lesson> lessons = this.lessonService.getAllLessons();
        ArrayList<CalendarViewModel> lessonArrayModels = new ArrayList<>();
        for (Lesson lesson : lessons)
        {
            // Currently a glitch with usernames contained in other usernames, but will be fixed at later time
            if (lesson.getStudentList().contains(userDetails.getUsername()))
            {
                String lessonType = "";
                String lessonColor = "";
                if (lesson.getLessonType() == LessonType.THEORY_LESSON){
                    lessonType = "Theory lesson";
                    lessonColor = "CYAN";
                }
                if (lesson.getLessonType() == LessonType.PRACTICAL_LESSON){
                    lessonType = "Pratical lesson";
                    lessonColor = "GREEN";
                }
                CalendarViewModel calendarViewModel = new CalendarViewModel(lesson.getCourseId(), lessonColor,lessonType, lesson.getLessonDate());

                lessonArrayModels.add(calendarViewModel);
            }
        }
        return lessonArrayModels;
    }

}
