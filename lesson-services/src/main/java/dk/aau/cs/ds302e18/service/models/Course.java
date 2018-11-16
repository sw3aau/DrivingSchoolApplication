package dk.aau.cs.ds302e18.service.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="COURSE")
public class Course
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COURSE_TICK")
    private long id;
    @Column(name="STUDENT_USERNAMES")
    private String studentUsernames;

    private Date startDate;
    private ArrayList<Integer> weekdays;
    private int numberLessons;
    private int numberLessonsADay;
    private ArrayList<String> studentList;
    private String location;
    private String instructorName;
    private int lessonType;
    private List<String> StudentNameList;

    public Course() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStudentUsernames() {
        return studentUsernames;
    }

    public void setStudentUsernames(String studentUsernames) {
        this.studentUsernames = studentUsernames;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public ArrayList<Integer> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(ArrayList<Integer> weekdays) {
        this.weekdays = weekdays;
    }

    public int getNumberLessons() {
        return numberLessons;
    }

    public void setNumberLessons(int numberLessons) {
        this.numberLessons = numberLessons;
    }

    public int getNumberLessonsADay() {
        return numberLessonsADay;
    }

    public void setNumberLessonsADay(int numberLessonsADay) {
        this.numberLessonsADay = numberLessonsADay;
    }

    public ArrayList<String> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<String> studentList) {
        this.studentList = studentList;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public int getLessonType() {
        return lessonType;
    }

    public void setLessonType(int lessonType) {
        this.lessonType = lessonType;
    }

    public List<String> getStudentNameList() {
        return StudentNameList;
    }

    public void setStudentNameList(List<String> studentNameList) {
        StudentNameList = studentNameList;
    }
}
