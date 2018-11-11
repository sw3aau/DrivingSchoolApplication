package dk.aau.cs.ds302e18.app;

import java.sql.Date;

public class LessonModel
{
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    private int id;

    public LessonModel(int id, String color, String title, Date start)
    {
        this.id = id;
        this.color = color;
        this.title = title;
        this.start = start;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    private String color;

    public void setTitle(String title)
    {
        this.title = title;
    }

    private String title;


    public String getTitle()
    {
        return title;
    }


    public Date getStart()
    {
        return start;
    }

    public void setStart(Date start)
    {
        this.start = start;
    }

    private Date start;
}
