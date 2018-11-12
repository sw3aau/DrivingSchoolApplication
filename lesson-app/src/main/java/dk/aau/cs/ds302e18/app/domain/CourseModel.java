package dk.aau.cs.ds302e18.app.domain;

import java.util.ArrayList;
import java.util.Date;

public class CourseModel {
    private Date startDate;
    private ArrayList<Integer> weekdays;
    private int numberLessons;
    private int numberLessonsADay;
    private ArrayList<String> studentList;
    private int courseTick;
    private String location;
    private String instructorName;
    private int lessonType;

    private String studentUsernames;



    public String getStudentUsernames() {
        return studentUsernames;
    }

    public void setStudentUsernames(String studentUsernames) {
        this.studentUsernames = studentUsernames;
    }


    public Course translateModelToCourse(){
        Course course = new Course();
        course.setStartDate(this.startDate);
        course.setWeekdays(this.weekdays);
        course.setNumberLessons(this.numberLessons);
        course.setNumberLessonsADay(this.numberLessonsADay);
        course.setStudentList(this.studentList);
        course.setCourseTick(this.courseTick);
        course.setLocation(this.location);
        course.setInstructorName(this.instructorName);
        course.setLessonType(this.lessonType);
        course.setStudentUsernames(this.studentUsernames);

        return course;
    }
}
