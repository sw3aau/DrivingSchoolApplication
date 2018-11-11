package dk.aau.cs.ds302e18.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/")
public class CalendarController
{
    @GetMapping(value = "/calendar")
    public String getCalendar(Model model)
    {
        return "calendar";
    }

    @RequestMapping("/getData")
    public @ResponseBody LessonModel getCalendarData(){
        LessonModel lessonModel = new LessonModel();
        lessonModel.setStart("2018-01-09T12:30:00");
        lessonModel.setTitle("titlehere");
        return lessonModel;
    }
}
