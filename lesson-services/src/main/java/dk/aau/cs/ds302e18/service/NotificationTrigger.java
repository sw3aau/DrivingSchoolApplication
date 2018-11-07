package dk.aau.cs.ds302e18.service;

import java.util.Date;
import java.util.Timer;

//Timer class to trigger NotificationTriggerTask once every 24 hour period.
public class NotificationTrigger {
    private Timer timer;

    public NotificationTrigger() {
    }

    //Starts the timer.
    //Takes a Date and long as arguments for initializing the timer.
    //If the date is in the past, then any "missed" executions will be scheduled for immediate "catch up" execution.
    //https://docs.oracle.com/javase/7/docs/api/java/util/Timer.html#scheduleAtFixedRate(java.util.TimerTask,%20java.util.Date,%20long)
    public void startNotificationSystem(Date timerStartDate, long taskDelayInMilliseconds) {
        timer = new Timer();
        NotificationTriggerTask task = new NotificationTriggerTask();
        timer.scheduleAtFixedRate(task, timerStartDate, taskDelayInMilliseconds);
    }

    //Stops the timer.
    public void stopNotificationSystem() {
        timer.cancel();
    }
}
