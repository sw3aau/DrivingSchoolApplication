package dk.aau.cs.ds302e18.app.domain;

public class Signature
{
    private long id;
    private long lessonId;
    private String studentSignature;
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
