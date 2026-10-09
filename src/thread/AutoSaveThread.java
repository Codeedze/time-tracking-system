package thread;

import model.TimeLog;
           import service.TimeLogService;
   
           import service.TimeLogServiceImpl;
import util.TimeLogManager;
        
import exception.InvalidTimeLogException;

import java.util.List;

public class AutoSaveThread extends Thread {

    private volatile boolean running = true;
         
    private final TimeLogService logService = new TimeLogServiceImpl();
    
                private final TimeLogManager manager = TimeLogManager.getInstance();

    public AutoSaveThread() {
     
        setName("AutoSaveThread");
        setDaemon(true); // dies with the app
    }

    @Override
    public void run() {
        System.out.println("[" + getName() + "] Started. Auto-saving every 30 seconds...");

        while (running) {
            try {
           
                Thread.sleep(30_000); // 30 seconds
                saveNow();
            } catch (InterruptedException e) {
                System.out.println("[" + getName() + "] Interrupted.");
           
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    private void saveNow() {
        List<TimeLog> pending = manager.drainPendingLogs();
        if (pending.isEmpty()) {
          
            System.out.println("[" + getName() + "] Nothing to save.");
            return;
        }

        System.out.println("[" + getName() + "] Saving " + pending.size() + " log(s)...");
        for (TimeLog log : pending) {
            try {
                logService.addLog(log);
          
            } catch 
            (InvalidTimeLogException e) {
                System.out.println("[" + getName() + "] Skipped invalid log: " + e.getMessage());
            }
        }
        System.out.println("[" + getName() + "] Auto-save complete.");
    }

    public void shutdown() {
        running = false;
        this.interrupt();
    }
}