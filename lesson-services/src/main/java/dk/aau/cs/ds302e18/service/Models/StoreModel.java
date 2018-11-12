package dk.aau.cs.ds302e18.service.Models;

public class StoreModel
{
    private long courseId;
    private String studentUsername;
    private byte state;

    public long getCourseId()
    {
        return courseId;
    }

    public void setCourseId(long courseId)
    {
        this.courseId = courseId;
    }

    public String getStudentUsername()
    {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername)
    {
        this.studentUsername = studentUsername;
    }

    public byte getState()
    {
        return state;
    }

    public void setState(byte state)
    {
        this.state = state;
    }

    public Store translateModelToStore()
    {
        Store store = new Store();
        store.setCourseId(this.courseId);
        store.setStudentUsername(this.studentUsername);
        store.setState(this.state);
        return store;
    }
}

