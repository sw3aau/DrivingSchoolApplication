package dk.aau.cs.ds302e18.service.Models;

import javax.persistence.*;

@Entity
@Table(name="STORE")
public class Store
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REQUEST_ID")
    private long id;
    @Column(name = "COURSE_ID")
    private long courseId;
    @Column(name = "STUDENT")
    private String studentUsername;
    @Column(name = "STATE")
    private byte state;

    public Store()
    {
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
}

