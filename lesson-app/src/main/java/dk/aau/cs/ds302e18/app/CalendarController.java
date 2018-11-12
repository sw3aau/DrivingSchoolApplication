package dk.aau.cs.ds302e18.app;

import dk.aau.cs.ds302e18.app.domain.CalendarViewModel;
import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.domain.LessonModel;
import dk.aau.cs.ds302e18.app.service.LessonService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
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
            if (lesson.getStudentList().contains(userDetails.getUsername()))
            {
                String lessonType = "";
                String lessonColor = "";
                if (lesson.getLessonType() == 1){
                    lessonType = "Theory lesson";
                    lessonColor = "CYAN";
                }
                if (lesson.getLessonType() == 2){
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
