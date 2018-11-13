package dk.aau.cs.ds302e18.service.models;

import javax.persistence.*;

@Entity
@Table(name="SIGNATURE")
public class Signature
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SIGNATURE_ID")
    private long id;
    @Column(name="LESSON_ID")
    private long lessonId;
    @Column(name="STUDENT_SIGN")
    private String studentSignature;
    @Column(name="INSTRUCTOR_SIGN")
    private String instructorSignature;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getLessonId()
    {
        return lessonId;
    }

    public void setLessonId(long lessonId)
    {
        this.lessonId = lessonId;
    }

    public String getStudentSignature()
    {
        return studentSignature;
    }

    public void setStudentSignature(String studentSignature)
    {
        this.studentSignature = studentSignature;
    }

    public String getInstructorSignature()
    {
        return instructorSignature;
    }

    public void setInstructorSignature(String instructorSignature)
    {
        this.instructorSignature = instructorSignature;
    }
}
