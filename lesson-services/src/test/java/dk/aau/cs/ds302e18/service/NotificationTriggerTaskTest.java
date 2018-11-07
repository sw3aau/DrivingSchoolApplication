package dk.aau.cs.ds302e18.service;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class NotificationTriggerTaskTest {

    @Test
    public void dateTest() {
        Date currdate = new Date(2018,10,5,14,52,40);

        String formattetDate = currdate.getYear() + "-" + currdate.getMonth() + "-" + currdate.getDate()
                + " " + currdate.getHours() + ":" + currdate.getMinutes() + ":" + currdate.getSeconds();
        assertEquals("2018-10-5 14:52:40", formattetDate);
    }

    @Test
    public void stringSplitTest() {
        String time = "2018-05-20 15:40:20";
        String[] stringArray = time.split(" ");
        assertEquals("2018-05-20",stringArray[0]);
        assertEquals("15:40:20", stringArray[1]);
    }

    @Test
    public void fullMessageTest() {
        String username = "Sembrik";
        String lessonDate = "2018-05-20 15:40:20";
        String lessonLocation = "Cassiopeia";
        String[] stringArray = lessonDate.split(" ");

        String fullMessage = username + " your lesson tomorrow (" + stringArray[0] + ") is at " + lessonLocation + ", at the time: " + stringArray[1] + ".";
        assertEquals("Sembrik your lesson tomorrow (2018-05-20) is at Cassiopeia, at the time: 15:40:20.", fullMessage);
    }

}