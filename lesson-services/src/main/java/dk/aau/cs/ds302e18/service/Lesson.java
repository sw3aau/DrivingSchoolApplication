package dk.aau.cs.ds302e18.service;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="LESSON")
public class Lesson
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LESSON_ID")
    private long id;
    @Column(name="LESSON_TYPE")
    private byte lessonType;
    @Column(name="STUDENT_LIST")
    private String studentList;
    @Column(name="INSTRUCTOR")
    private String lessonInstructor;
    @Column(name="LESSON_DATE")
    private Date lessonDate;
    @Column(name="LESSON_LOCATION")
    private String lessonLocation;
    @Column(name="STATE")
    private boolean isSigned;

    public Lesson() {
        super();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public byte getLessonType()
    {
        return lessonType;
    }

    public void setLessonType(byte lessonType)
    {
        this.lessonType = lessonType;
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

    public boolean isSigned()
    {
        return isSigned;
    }

    public void setSigned(boolean signed)
    {
        isSigned = signed;
    }
}
