package dk.aau.cs.ds302e18.service;

import java.util.TimerTask;

public class TimerTaskTest extends TimerTask {
    @Override
    public void run() {
        System.out.println("Fuck timers.");
    }
}
