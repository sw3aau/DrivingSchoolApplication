package dk.aau.cs.ds302e18.service;

import org.junit.Test;

import java.util.Date;

public class NotificationTriggerTest {

    @Test
    public void startNotificationSystemTest() {
        //Creates a new NotificationTrigger and starts a timer with a new Date and a delay of 10 seconds (10000 milliseconds).
        //Without trigger.stopNotificationSystem(); this test runs forever and will need to be terminated.
        NotificationTrigger trigger = new NotificationTrigger();
        Date timerStartDate = new Date();
        trigger.startNotificationSystem(timerStartDate,10000);
        //trigger.stopNotificationSystem();
    }
}