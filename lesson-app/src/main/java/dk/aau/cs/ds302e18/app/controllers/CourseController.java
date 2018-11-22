package dk.aau.cs.ds302e18.app.controllers;


import dk.aau.cs.ds302e18.app.auth.Account;
import dk.aau.cs.ds302e18.app.auth.AccountRespository;
import dk.aau.cs.ds302e18.app.auth.AuthGroup;
import dk.aau.cs.ds302e18.app.auth.AuthGroupRepository;
import dk.aau.cs.ds302e18.app.domain.Course;
import dk.aau.cs.ds302e18.app.domain.CourseModel;
import dk.aau.cs.ds302e18.app.domain.LessonModel;
import dk.aau.cs.ds302e18.app.domain.LessonType;
import dk.aau.cs.ds302e18.app.service.CourseService;
import dk.aau.cs.ds302e18.app.service.LessonService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping("/")
public class CourseController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final AccountRespository accountRespository;
    private final AuthGroupRepository authGroupRepository;

    public CourseController(CourseService courseService, LessonService lessonService, AccountRespository accountRespository, AuthGroupRepository authGroupRepository) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.accountRespository = accountRespository;
        this.authGroupRepository = authGroupRepository;
    }

    @GetMapping(value = "/course")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCourses(Model model)
    {
        List<Course> courses = this.courseService.getAllCourseRequests();
        ArrayList<Account> studentAccounts = findAccountsOfType("USER");

        setFullNamesFromUsernamesString(courses);
        model.addAttribute("studentAccounts", studentAccounts);
        model.addAttribute("courses", courses);
        return "courses-view";
    }


    @PostMapping(value = "/course/addCourse")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCourse(@ModelAttribute CourseModel courseModel) {
        System.out.println(courseModel.toString());
        courseService.addCourse(courseModel);
        return new ModelAndView("redirect:/course/courseAddLessons");
    }

    /* When an course has been created the instructor will be redirected to this site, where they can decide
      * if they want to assign initial theory lessons. */
    @GetMapping(value = "/course/courseAddLessons")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getCourseAddLessonsForm(Model model)
    {
        List<Course> courses = this.courseService.getAllCourseRequests();
        setFullNamesFromUsernamesString(courses);
        ArrayList<Account> instructorAccounts = findAccountsOfType("ADMIN");

        model.addAttribute("instructorAccounts", instructorAccounts);
        model.addAttribute("courses", courses);
        return "course-add-lessons-view";
    }

    @PostMapping(value = "/course/courseAddLessons")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView courseAddLessons(@ModelAttribute CourseModel courseModel) {
        ArrayList<Date> lessonDates = createLessonDates(courseModel.getStartDate(), courseModel.getWeekdays(), courseModel.getNumberLessons(), courseModel.getNumberLessonsADay());
        /* All added lessons will be initialized as unsigned */
        boolean isSigned = false;

        /* For every lesson date, a lesson will be created */
        for (int j = 0; j < lessonDates.size(); j++) {
            Date lessonDate = lessonDates.get(j);
            LessonModel lesson = new LessonModel();
            lesson.setSigned(isSigned);
            lesson.setLessonDate(lessonDate);
            lesson.setLessonInstructor(courseModel.getInstructorUsername());
            lesson.setLessonLocation(courseModel.getLocation());
            Course latestCreatedCourse = courseService.getLastCourseByID();
            lesson.setStudentList(latestCreatedCourse.getStudentUsernames());
            lesson.setCourseId(latestCreatedCourse.getCourseTableID());
            lesson.setLessonType(LessonType.THEORY_LESSON);

            lessonService.addLesson(lesson);
        }
        return new ModelAndView("redirect:/lessons");
    }


    @GetMapping(value = "/course/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView deleteCourse(@PathVariable long id){
        courseService.deleteCourse(id);
        return new ModelAndView("redirect:/course/");
    }

    /* Posts a newly added lesson in the lessons list on the website */
    @PostMapping(value = "/course")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addCourse(HttpServletRequest request, Model model, @ModelAttribute CourseModel courseModel)
    {
        /* The newly added course object is retrieved from the 8100 server.  */
        Course course = this.courseService.addCourse(courseModel);
        if (course.getStudentUsernames().isEmpty() | course.getCourseTableID() == 0)
        {
            throw new RuntimeException();
        }
        model.addAttribute("course", course);

        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/course/" + course.getCourseTableID());
    }



/*
    @GetMapping(value = "/course/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getCourse(Model model, @PathVariable long id)
    {
        Course course = this.courseService.getCourse(id);
        ArrayList<Account> instructorAccounts = findAccountsOfType("ADMIN");
        model.addAttribute("instructorAccounts", instructorAccounts);
        model.addAttribute("course", course);
        return "course-view";
    }
*/
/*
    @PostMapping(value = "/course/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateCourse(Model model, @PathVariable long id, @ModelAttribute CourseModel courseModel)
    {
        Course course = this.courseService.updateCourse(id, courseModel);
        model.addAttribute("course", course);
        model.addAttribute("courseModel", new CourseModel());
        return "course-view";
    }
*/
    public ArrayList<Date> createLessonDates(Date startDate, ArrayList<Integer> weekdays, int numberLessonsToDistribute,
                                             int numberLessonsADay) {
        ArrayList<Date> lessonDates = new ArrayList<>();
        Date currentDayDate = startDate;
        int dayCount = 0;
        /* A lesson should minimum be 45 minutes according to law, and the two interviewed driving schools had a 45
           minute lesson duration. */
        int lessonDurationMinutes = 45;

        int oneMinuteInMilliseconds = 60000;
        int lessonDurationMilliseconds = oneMinuteInMilliseconds * lessonDurationMinutes;

        /* While all lessons has not yet been distributed */
        while (numberLessonsToDistribute > 0) {
            /* 86400000 * dayCount is not contained in a variable due to the possible of reaching an overflow of most
               data-types. Date is by default suitable to handle very large numbers. */
            currentDayDate = new Date(startDate.getTime() + 86400000 * dayCount);
            if (weekdays.contains(currentDayDate.getDay())) {
                /* If it is one of the weekdays specified in the weekdays array, add an number of lessons equal to
                 * number lessons a day. Also since it adds a several lessons in a loop it needs to check before each lesson
                 * is added if the necessary lessons have been distributed. */
                for (int g = 0; g < numberLessonsADay; g++) {
                    if (numberLessonsToDistribute > 0) {
                        System.out.println(g);
                        lessonDates.add(new Date(currentDayDate.getTime() + lessonDurationMilliseconds * g));
                        numberLessonsToDistribute--;
                    }
                }
            }
            //System.out.println(dayCount);
            dayCount += 1;
        }
        return lessonDates;
    }

    private String saveStringListAsSingleString(ArrayList<String> studentNameList) {
        String combinedString = "";
        for (String student : studentNameList) {
            combinedString += student + ",";
        }
        return combinedString;
    }

    private ArrayList<String> saveUsernameStringAsList(String usernames) {
        ArrayList<String> studentList = new ArrayList<>();
        String[] parts = usernames.split(",");
        for(String part: parts){
            studentList.add(part);
        }
        return studentList;
    }

    private ArrayList<Account> findAccountsOfType(String accountType){
        List<AuthGroup> users = authGroupRepository.findAll();
        ArrayList<AuthGroup> studentsAuthList = new ArrayList<>();
        for(AuthGroup user: users){
            if(user.getAuthGroup().equals(accountType)){
                studentsAuthList.add(user);
            }
        }
        ArrayList<Account> studentAccounts = new ArrayList<>();
        for(AuthGroup studentAuth : studentsAuthList){
            studentAccounts.add(accountRespository.findByUsername(studentAuth.getUsername()));
        }
        return studentAccounts;
    }

    private void setFullNamesFromUsernamesString(List<Course> courseList){
        /*  Every student username list in account is separated and added to an String array. Then the
         *  username is used to fetch the full name of the student belonging to the username. */
        for(Course course: courseList){
            ArrayList<String> fullNames = new ArrayList<>();
            ArrayList<String> listOfUsernames = saveUsernameStringAsList(course.getStudentUsernames());
            for(String username: listOfUsernames){
                String firstName = accountRespository.findByUsername(username).getFirstName();
                String lastName = accountRespository.findByUsername(username).getLastName();
                String fullName = firstName + " " + lastName;
                fullNames.add(" " + fullName);
            }
            String studentFullNames = saveStringListAsSingleString(fullNames);
            /* The way saveStringListAsSingleString formats the list to an string is with an comma at the end of each
               object. The last of the commas is removed for appearance sake. */
            String studentFullNamesWithoutEndingComma = studentFullNames.substring(0, studentFullNames.length()-1);

            course.setStudentNamesString(studentFullNamesWithoutEndingComma);
        }
    }
}
