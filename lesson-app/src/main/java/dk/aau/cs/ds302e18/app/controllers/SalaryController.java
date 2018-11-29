package dk.aau.cs.ds302e18.app.controllers;

import dk.aau.cs.ds302e18.app.SortLessonsByCourseId;
import dk.aau.cs.ds302e18.app.auth.Account;
import dk.aau.cs.ds302e18.app.auth.AccountRespository;
import dk.aau.cs.ds302e18.app.auth.AuthGroup;
import dk.aau.cs.ds302e18.app.auth.AuthGroupRepository;
import dk.aau.cs.ds302e18.app.domain.Course;
import dk.aau.cs.ds302e18.app.domain.CourseType;
import dk.aau.cs.ds302e18.app.domain.Lesson;
import dk.aau.cs.ds302e18.app.domain.LessonState;
import dk.aau.cs.ds302e18.app.service.CourseService;
import dk.aau.cs.ds302e18.app.service.LessonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Controller
@RequestMapping
public class SalaryController {
    private final LessonService lessonService;
    private final CourseService courseService;
    private final AccountRespository accountRespository;
    private final AuthGroupRepository authGroupRepository;

    public SalaryController(LessonService lessonService, CourseService courseService, AccountRespository accountRespository, AuthGroupRepository authGroupRepository) {
        super();
        this.lessonService = lessonService;
        this.courseService = courseService;
        this.accountRespository = accountRespository;
        this.authGroupRepository = authGroupRepository;
    }

    @GetMapping(value = {"/salary"})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getInstructorLogbookLessons(Model model) {
        List<Lesson> lessonList = this.lessonService.getAllLessons();
        //Creates a list, to store the admin's completed lessons for the salary
        List<Lesson> salaryLessonList = new ArrayList<>();
        //Creates three ints to store the number of lessons of a specific courseType
        //and an int to store the total number of unique course ids from the found lessons
        int courseTypeBLessonTotal = 0, courseTypeBELessonTotal = 0, courseTypeALessonTotal = 0, uniqueCourseIdTotal = 0;

        //Creates a date and sets it's date, hour, minute and seconds to a specific time of the month of the year
        //The format will be yy-mm-20 23:59:00, which will be used to find lessons after this date
        Date thisMonthDate = new Date();
        thisMonthDate.setDate(20);
        thisMonthDate.setHours(23);
        thisMonthDate.setMinutes(59);
        thisMonthDate.setSeconds(0);

        //Creates a date and sets it's to the same as thisMonthDate and moves it's month, one month forward
        //The format will be yy-mm-20 23:59:00, which will be used to find lessons after this date
        Date nextMonthDate = new Date(thisMonthDate.getTime());
        nextMonthDate.setMonth(thisMonthDate.getMonth() + 1);

        //Fetches the username from the session
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();

        //Checks all lessons and finds any which fits within a specific time period,
        //if the instructor is the same as the instructor for the lesson and if the lesson is in a completed state
        for (Lesson lesson : lessonList) {
            if (lesson.getLessonDate().after(thisMonthDate) && lesson.getLessonDate().before(nextMonthDate)) {
                if (lesson.getLessonInstructor().equals(username) && ((lesson.getLessonState().equals(LessonState.COMPLETED_UNSIGNED)) ||
                        (lesson.getLessonState().equals(LessonState.COMPLETED_SIGNED)) ||
                        (lesson.getLessonState().equals(LessonState.STUDENT_ABSENT)))) {
                    //Adds the lesson to salaryLessonList
                    salaryLessonList.add(lesson);
                    //Checks which type of course the lesson is in and counts up the total of that type
                    Course course = courseService.getCourse(lesson.getCourseId());
                    if (course.getCourseType().equals(CourseType.TYPE_B_CAR)) {
                        courseTypeBLessonTotal++;
                    }
                    if (course.getCourseType().equals(CourseType.TYPE_BE_CAR_TRAILER)) {
                        courseTypeBELessonTotal++;
                    }
                    if (course.getCourseType().equals(CourseType.TYPE_A_BIKE)) {
                        courseTypeALessonTotal++;
                    }
                }
            }
        }

        //Sorts the founds lessons by id and iterates through them to find unique course ids
        salaryLessonList.sort(new SortLessonsByCourseId());
        long currCourseId;
        Long nextCourseId = null;
        for (Lesson lesson : salaryLessonList) {
            currCourseId = lesson.getCourseId();
            if(nextCourseId == null) {
                uniqueCourseIdTotal++;
                nextCourseId = currCourseId;
            }
            if(nextCourseId != currCourseId) {
                uniqueCourseIdTotal++;
                nextCourseId = currCourseId;
            }
        }

        //Finds all accounts and compares their auth group to find the ones who are instructors
        List<AuthGroup> authGroups = this.authGroupRepository.findAll();
        List<Account> accountList = this.accountRespository.findAll();
        List<Account> instructorList = new ArrayList<>();
        for (int i = 0; i < accountList.size(); i++) {
            if (authGroups.get(i).getAuthGroup().equals("INSTRUCTOR")) {
                instructorList.add(accountList.get(i));
            }
        }

        //Models variables from java for usage in html
        model.addAttribute("instructorList", instructorList);
        model.addAttribute("uniqueCourseIdTotal", uniqueCourseIdTotal);
        model.addAttribute("courseTypeBLessonTotal", courseTypeBLessonTotal);
        model.addAttribute("courseTypeBELessonTotal", courseTypeBELessonTotal);
        model.addAttribute("courseTypeALessonTotal", courseTypeALessonTotal);
        model.addAttribute("salaryLessonList", salaryLessonList);
        model.addAttribute("salaryLessonTotal", salaryLessonList.size());
        model.addAttribute("username", username);
        return "salary";
    }



    @ModelAttribute("gravatar")
    public String gravatar() {

        //Models Gravatar
        System.out.println(accountRespository.findByUsername(getAccountUsername()).getEmail());
        String gravatar = ("http://0.gravatar.com/avatar/"+md5Hex(accountRespository.findByUsername(getAccountUsername()).getEmail()));
        return (gravatar);
    }

    public String getAccountUsername()
    {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return username;
    }
}
