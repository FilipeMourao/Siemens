package de.tum.ipraktikum.utils;

import java.util.Timer;
import java.util.TimerTask;

public abstract class ResettableTimer {

    private Timer timer;
    private long timerDelay;
    private String processId;

    public ResettableTimer(long timerDelay, String processId) {
        this.timerDelay = timerDelay;
        this.processId = processId;
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                runPeriodic();
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, timerDelay);
    }

    protected String getProcessId() {
        return this.processId;
    }

    public void reset() {
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                runPeriodic();
            }
        };
        timer.cancel();
        timer = new Timer();
        timer.schedule(timerTask, timerDelay);
        System.out.println("Updated timer");
    }

    public void cancel() {
        timer.cancel();
    }

    public abstract void runPeriodic();

}
