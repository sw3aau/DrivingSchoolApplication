package dk.aau.cs.ds302e18.app;

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

@Controller
public class CalendarController
{
    @GetMapping(value = "/calendar")
    public String getCalendar(Model model)
    {
        return "calendar";
    }

    @RequestMapping("/calendar/getData")
    public @ResponseBody ArrayList<LessonModel> getCalendarData(){

        // Get the Spring Security user details of the currently logged in user
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ArrayList<LessonModel> lessonModelArrayList = new ArrayList<>();

        Connection connection = new DBConnector().createConnectionObject();

        String query = "SELECT * FROM lesson";

        try
        {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            if (rs == null)
                return null;


            while (rs.next())
            {
                String[] splitUsernames = rs.getString("student_list").split(":");

                int i = 0; // This value is the id of the lesson. Since the ids are only for each user, it can simply start from 0 and iterate up.
                for (String username : splitUsernames)
                {
                    if (username.equals(userDetails.getUsername()))
                    {
                        LessonModel lessonModel = new LessonModel(i, "red","Lesson", rs.getTimestamp("lesson_date").toLocalDateTime());
                        lessonModelArrayList.add(lessonModel);
                        ++i;
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.getCause();
        }

        return lessonModelArrayList;
    }
}
