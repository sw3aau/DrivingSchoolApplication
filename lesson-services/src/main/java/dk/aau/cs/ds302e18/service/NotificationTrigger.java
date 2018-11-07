package dk.aau.cs.ds302e18.service;

import java.util.Date;
import java.util.Timer;

//Timer class to trigger NotificationTriggerTask once every 24 hour period.
public class NotificationTrigger {
    private Timer timer;

    public NotificationTrigger() {
    }

    //Starts the timer and sets the trigger time to 20:00:00 every day.
    public void startNotificationSystem() {
        Date date = new Date();
        timer = new Timer();
        NotificationTriggerTask task = new NotificationTriggerTask();

        //Sets time of the date to 20:00:00, which is the timer for the notifications to be sent.
        date.setHours(11);
        date.setMinutes(0);
        date.setSeconds(0);

        //86400000 is 24 hours in milliseconds.
        timer.scheduleAtFixedRate(task, date, 900000);
    }

    //Stops the timer.
    public void stopNotificationSystem() {
        timer.cancel();
    }
}
