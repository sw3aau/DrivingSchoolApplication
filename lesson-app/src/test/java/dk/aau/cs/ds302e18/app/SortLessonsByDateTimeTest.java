package dk.aau.cs.ds302e18.app;

import dk.aau.cs.ds302e18.app.domain.Lesson;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class SortLessonsByDateTimeTest {

    private List<Lesson> lessonList = new ArrayList<>();
    private Lesson lesson1 = new Lesson();
    private Lesson lesson2 = new Lesson();
    private Lesson lesson3 = new Lesson();

    @Before
    public void setUp() throws Exception {
        lesson1.setLessonDate(new Date(2018, 10, 20, 13, 00, 00));
        lesson2.setLessonDate(new Date(2018, 10, 20, 10, 00, 00));
        lesson3.setLessonDate(new Date(2018, 10, 20, 15, 00, 00));

        lessonList.add(lesson1);
        lessonList.add(lesson2);
        lessonList.add(lesson3);
    }

    @Test
    public void name() {
        lessonList.sort(new SortLessonsByDateTime());
        assertEquals(lessonList.get(0).getLessonDate(), (lesson2.getLessonDate()));
        assertEquals(lessonList.get(1).getLessonDate(), (lesson1.getLessonDate()));
        assertEquals(lessonList.get(2).getLessonDate(), (lesson3.getLessonDate()));

    }
}