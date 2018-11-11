package dk.aau.cs.ds302e18.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class MockitoTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonServicesController.class);
    private final LessonRepository repository;

    public MockitoTest(LessonRepository repository) {
        super();
        this.repository = repository;
    }

    public Lesson findLessonWithGreatestID() {
        ArrayList<Lesson> lessonList = new ArrayList<>(this.repository.findAll());
        Lesson lessonWIthGreatestID = new Lesson();
        Long greatestID = new Long(0);
        for(int i = 0; i<lessonList.size(); i++) {
            if(lessonList.get(i).getId() > greatestID) {
                greatestID = lessonList.get(i).getId();
                lessonWIthGreatestID = lessonList.get(i);
            }
        }
        return lessonWIthGreatestID;
    }
}
