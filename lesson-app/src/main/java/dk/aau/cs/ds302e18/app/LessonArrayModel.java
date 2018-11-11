package dk.aau.cs.ds302e18.app;

import java.util.ArrayList;

public class LessonArrayModel
{
    private int id;
    private ArrayList<LessonModel> lessonModelArrayList = new ArrayList<>();

    public LessonArrayModel(int id)
    {
        this.id = id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public ArrayList<LessonModel> getLessonModelArrayList()
    {
        return lessonModelArrayList;
    }

    public void setLessonModelArrayList(ArrayList<LessonModel> lessonModelArrayList)
    {
        this.lessonModelArrayList = lessonModelArrayList;
    }

}
