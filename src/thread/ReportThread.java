package thread;

import service.TimeLogService;
import service.TimeLogServiceImpl;

public class ReportThread extends Thread {

    private final int userId;
               private volatile double result = 0;
    private volatile boolean done = false;

    private final TimeLogService logService = new TimeLogServiceImpl();

                 public ReportThread(int userId) {
        this.userId = userId;
        setName("ReportThread-" + userId);
    }

    @Override
                  public void run() {
        System.out.println("[" + getName() + "] Generating report for user " + userId + "...");
        try {
            Thread.sleep(2000); // simulate heavy report computation
                      } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        result = logService.getTotalHours(userId);
                     done = true;
        System.out.println("[" + getName() + "] Done. Total hours = " + result);
    }

             public boolean isDone() { return done; }
    public double getResult() { return result; }
}