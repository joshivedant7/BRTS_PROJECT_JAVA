import java.util.Calendar;

public class TimeSpeeder extends Thread {
    long simulatedTime = System.currentTimeMillis();
    double speedFactor;
    TimeSpeeder(double speedFactor) {
        this.speedFactor = speedFactor;
    }

    @Override
    public void run(){
        long startTime = System.currentTimeMillis();
        while (true) {
            long realElapsedTime = System.currentTimeMillis() - startTime;
            simulatedTime = (long) (realElapsedTime * speedFactor);

            String timeString = formatTime(simulatedTime);

            UserModul.time = timeString;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    String formatTime(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
