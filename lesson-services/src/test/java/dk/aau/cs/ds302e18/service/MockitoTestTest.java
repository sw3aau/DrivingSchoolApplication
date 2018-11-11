package dk.aau.cs.ds302e18.service;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

public class MockitoTestTest {
    @Test
    public void testFindTheGreatestFromAllData() {
        LessonRepository lessonRepositoryMock =  mock(LessonRepository.class);
        Lesson mock1 = new Lesson();
        Lesson mock2 = new Lesson();
        Lesson mock3 = new Lesson();
        mock1.setId(1);
        mock1.setStudentList("studentUsername1");
        mock2.setId(2);
        mock1.setStudentList("studentUsername2");
        mock3.setId(3);
        mock3.setStudentList("studentUsername3");

        ArrayList<Lesson> lessonList = new ArrayList<>();
        lessonList.add(mock1);
        lessonList.add(mock2);
        lessonList.add(mock3);
        int testt = 3;
        ArrayList<Integer> intList = new ArrayList<>();
        intList.add(testt);
        /* Defines what findAll should return */
        when(lessonRepositoryMock.findAll()).thenReturn(lessonList);
        /* Testing method in MockitoTest */
        MockitoTest mockitoTest = new MockitoTest(lessonRepositoryMock);
        Long result = mockitoTest.findLessonWithGreatestID().getId();
        assertEquals(new Long(3), result);
        /* Testing method in lessonServiceController */

        //LessonServicesController lessonServicesController = new LessonServicesController(lessonRepositoryMock);
        //Lesson result2 = lessonServicesController.getLesson(new Long(2));
        //assertEquals(mock2, result2);
    }
}