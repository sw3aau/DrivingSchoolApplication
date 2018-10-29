package dk.aau.cs.ds302e18.app.domain;

import dk.aau.cs.ds302e18.app.Instructor;
import dk.aau.cs.ds302e18.app.Student;

import java.util.ArrayList;
import java.util.Date;

public class LessonModel
{
    private byte lessonType;
    private String studentList;
    private String lessonInstructor;
    private Date lessonDate;
    private String lessonLocation;
    //final so that lesson type cannot be changed once it's been set.
    //This is to prevent lessons from changing type after compeletion.
    //It lesson needs to be changed, a new lesson must be created.
    private boolean isSigned;

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

    public byte getLessonType()
    {
        return lessonType;
    }

    public void setLessonType(byte lessonType)
    {
        this.lessonType = lessonType;
    }

    Lesson translateModelToLesson(){
        Lesson lesson = new Lesson();
        lesson.setLessonType(this.lessonType);
        lesson.setStudentList(this.studentList);
        lesson.setLessonInstructor(this.lessonInstructor);
        lesson.setLessonDate(this.lessonDate);
        lesson.setLessonLocation(this.lessonLocation);
        lesson.setSigned(this.isSigned);
        return lesson;
    }
}
