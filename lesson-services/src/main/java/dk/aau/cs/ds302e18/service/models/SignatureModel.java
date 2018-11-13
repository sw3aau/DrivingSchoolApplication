package dk.aau.cs.ds302e18.service.models;

public class SignatureModel
{
    private long lessonId;
    private String studentSignature;
    private String instructorSignature;

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

    public Signature translateModelToSignature()
    {
        Signature signature = new Signature();
        signature.setLessonId(this.lessonId);
        signature.setStudentSignature(this.studentSignature);
        signature.setInstructorSignature(this.instructorSignature);
        return signature;
    }
}
