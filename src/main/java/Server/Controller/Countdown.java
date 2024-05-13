package Server.Controller;

import java.util.Timer;
import java.util.TimerTask;

public class Countdown {

    private Timer timer;
    private boolean isTimerOver;

    public Countdown() {
        this.timer = new Timer();
        this.isTimerOver = false;
    }

    public void start(long durationInMillis) {
        timer.scheduleAtFixedRate(new TimerTask() {
            private long startTime = System.currentTimeMillis();

            @Override
            public void run() {
                // Calculate elapsed time
                long elapsedTime = System.currentTimeMillis() - startTime;

                // Check if the duration has elapsed
                if (elapsedTime >= durationInMillis) {
                    isTimerOver = true;
                    cancel(); // Cancel the timer if the duration has elapsed
                }
            }
        }, 0, 200);
    }

    public void cancel() {
        timer.cancel();
    }

    public boolean isTimerOver() {
        return isTimerOver;
    }

}
