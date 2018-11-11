package dk.aau.cs.ds302e18.app;

public class LessonModel
{
    public void setTitle(String title)
    {
        this.title = title;
    }

    private String title;

    public void setStart(String start)
    {
        this.start = start;
    }

    public String getTitle()
    {
        return title;
    }

    public String getStart()
    {
        return start;
    }

    private String start;
}
