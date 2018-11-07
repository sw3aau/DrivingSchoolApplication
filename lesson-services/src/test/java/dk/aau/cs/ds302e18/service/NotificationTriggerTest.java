package dk.aau.cs.ds302e18.service;

import org.junit.Test;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

public class NotificationTriggerTest {

    @Test
    public void dateTest() {
        Date currdate = new Date(2018,10,5,14,52,40);

        String formattetDate = currdate.getYear() + "-" + currdate.getMonth() + "-" + currdate.getDate()
                + " " + currdate.getHours() + ":" + currdate.getMinutes() + ":" + currdate.getSeconds();
        assertEquals("2018-10-5 14:52:40", formattetDate);
    }

    @Test
    public void dateTest2() {
        Date date = new Date();

        date.setHours(20);
        date.setMinutes(0);
        date.setSeconds(0);
        System.out.println(date.toString());
    }

    @Test
    public void startNotificationSystemTest() {
        NotificationTrigger notificationTrigger = new NotificationTrigger();
        notificationTrigger.startNotificationSystem();
        notificationTrigger.stopNotificationSystem();
    }

    @Test
    public void timerTest() {
        Date date = new Date();
        Timer timer = new Timer();
        TimerTaskTest task = new TimerTaskTest();

        //Sets time of the date to 20:00:00, which is the timer for the notifications to be sent.
        date.setHours(11);
        date.setMinutes(0);
        date.setSeconds(0);

        //86400000 is 24 hours in milliseconds.
        timer.scheduleAtFixedRate(task, date, 20000);


    }
}
