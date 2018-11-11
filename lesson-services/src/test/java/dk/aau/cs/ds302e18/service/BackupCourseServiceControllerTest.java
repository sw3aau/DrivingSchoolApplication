package dk.aau.cs.ds302e18.service;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class BackupCourseServiceControllerTest {
    private ArrayList<String> studentList;
    private BackupTestableCourseServiceController backupTestableCourseServiceController;
    private Date startDate;

    @Before
    public void setUp() throws Exception {
        backupTestableCourseServiceController = new BackupTestableCourseServiceController();
        String user1 = "username1";

        String user2 = "username2";


        String user3 = "username3";

        studentList = new ArrayList<>();
        studentList.add(user1);
        studentList.add(user2);
        studentList.add(user3);
        startDate = new Date(118,11,1,16,0);
    }

    @Test
    public void saveStudentnamesAsString() {
        String combinedStudentnames = backupTestableCourseServiceController.saveStudentnamesAsString(studentList);
        assertEquals("username1:username2:username3:", combinedStudentnames);
    }


    //@Test
    //public void saveStringAsStudents() {
    //    String combinedStudentnames = backupTestableCourseServiceController.saveStudentnamesAsString(studentList);
    //    ArrayList<Student> returnedStudentList = backupTestableCourseServiceController.saveStringAsStudents(combinedStudentnames);
    //    /* Checks that the list has 3 objects */
    //    assertEquals(3, returnedStudentList.size());
    //    /* Checks that every user has the proper username */
    //    String usernamesFromEachAccount = "";
    //    for(Student usernames : returnedStudentList) {
    //        usernamesFromEachAccount += usernames.getStudentname() + " ";
    //    }
    //    assertEquals("username1 username2 username3 ", usernamesFromEachAccount);
    //}


    @Test
    public void createNewEmptyCourse() {
        backupTestableCourseServiceController.createEmptyCourse(5);
    }

    @Test
    public void createNewCourseWithInitialStudents(){
        backupTestableCourseServiceController.createEmptyCourse(7, studentList);
    }

    @Test
    public void addStudent() {
        backupTestableCourseServiceController.addStudent(7, studentList.get(1));
    }

    @Test
    public void deleteCourse() {
        backupTestableCourseServiceController.deleteCourse(3);
    }

    @Test
    public void createLessonDates() {
        ArrayList<Integer> lessonPlacementsFromOffset = new ArrayList<>();
        lessonPlacementsFromOffset.add(0);
        lessonPlacementsFromOffset.add(1);
        lessonPlacementsFromOffset.add(6);
        /* An uneven amount of lessons also checks that the if statement in the for loop prevents the loop from creating
           unnecessary extra lessons. */
        ArrayList<Date> dateList = backupTestableCourseServiceController.createLessonDates(startDate, lessonPlacementsFromOffset,5,2);
        for(Date date: dateList) {
            System.out.println("lesson date:" + date.getDate() + " weekday: " + date.getDay());
        }
    }

    @Test
    public void courseAddLessons() {
        ArrayList<Integer> weekdays = new ArrayList<>();
        weekdays.add(0);
        weekdays.add(1);
        backupTestableCourseServiceController.courseAddLessons(startDate,weekdays,3, 2, studentList, 7,"AAU", "Bin Yang", 1);
    }

    @Test public void springDBTest(){
        //backupTestableCourseServiceController.springDBTest();
    }
}