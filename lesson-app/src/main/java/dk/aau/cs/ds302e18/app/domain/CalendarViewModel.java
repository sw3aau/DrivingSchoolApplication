package dk.aau.cs.ds302e18.app.domain;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

public class CalendarViewModel
{
    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    private long id;

    public CalendarViewModel(long id, String color, String title, Date start)
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
