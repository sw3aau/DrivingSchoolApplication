package dk.aau.cs.ds302e18.service;

import dk.aau.cs.ds302e18.service.NotificationService.NextDayNotificationTask;
import dk.aau.cs.ds302e18.service.NotificationService.NotificationTimer;
import org.junit.Test;

import java.util.Date;

public class NotificationTimerTest {

    @Test
    public void startNotificationSystemTest() {
        //Creates a new NotificationTimer and starts a timer with a new Date and a delay of 10 seconds (10000 milliseconds).
        //Without trigger.stopNotificationSystem(); this test runs forever and will need to be terminated.
        NotificationTimer trigger = new NotificationTimer(new NextDayNotificationTask());
        Date timerStartDate = new Date();
        trigger.startNotificationSystem(timerStartDate,10000);
        //trigger.stopNotificationSystem();
    }
}