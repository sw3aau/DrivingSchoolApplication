package dk.aau.cs.ds302e18.app.domain;

import dk.aau.cs.ds302e18.app.Instructor;
import dk.aau.cs.ds302e18.app.Student;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

public class LessonModel
{
    private LessonType lessonType;

    private String studentList;

    private String lessonInstructor;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date lessonDate;

    private String lessonLocation;

    private boolean isSigned;

    private int courseId;

    public boolean isSigned()
    {
        return isSigned;
    }

    public void setSigned(boolean signed)
    {
        isSigned = signed;
    }

    public String getStudentList()
    {
        return studentList;
    }

    public void setStudentList(String studentList)
    {
        this.studentList = studentList;
    }

    public String getLessonInstructor()
    {
        return lessonInstructor;
    }

    public void setLessonInstructor(String lessonInstructor)
    {
        this.lessonInstructor = lessonInstructor;
    }

    public Date getLessonDate()
    {
        return lessonDate;
    }

    public void setLessonDate(Date lessonDate)
    {
        this.lessonDate = lessonDate;
    }

    public String getLessonLocation()
    {
        return lessonLocation;
    }

    public void setLessonLocation(String lessonLocation)
    {
        this.lessonLocation = lessonLocation;
    }

    public LessonType getLessonType()
    {
        return lessonType;
    }

    public void setLessonType(LessonType lessonType)
    {
        this.lessonType = lessonType;
    }

    public int getCourseId()
    {
        return courseId;
    }

    public void setCourseId(int courseId)
    {
        this.courseId = courseId;
    }

    Lesson translateModelToLesson(){
        Lesson lesson = new Lesson();
        lesson.setLessonType(this.lessonType);
        lesson.setStudentList(this.studentList);
        lesson.setLessonInstructor(this.lessonInstructor);
        lesson.setLessonDate(this.lessonDate);
        lesson.setLessonLocation(this.lessonLocation);
        lesson.setSigned(this.isSigned);
        lesson.setCourseId(this.courseId);
        return lesson;
    }
}
