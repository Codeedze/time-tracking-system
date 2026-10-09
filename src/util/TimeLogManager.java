package util;

import model.TimeLog;

import java.util.ArrayList;
import java.util.List;

public class TimeLogManager {

    private static TimeLogManager instance;

    // Shared list — accessed by GUI thread + autosave thread
    private final List<TimeLog> pendingLogs = new ArrayList<>();

    private TimeLogManager() {}

    public static synchronized TimeLogManager getInstance() {
      
        if (instance == null) {
            instance = new TimeLogManager();
        }
        return instance;
    }

    // Called by GUI thread
    public void addPendingLog(TimeLog log) {
      
      
        synchronized (pendingLogs) {
            pendingLogs.add(log);
           
            System.out.println("[Manager] Added pending log: " + log.getTask() +
                               " (queue size = " + pendingLogs.size() + ")");
        }
    }

    // Called by autosave thread — returns a copy and clears
    public List<TimeLog> drainPendingLogs() {
        
        
        synchronized (pendingLogs) {
            List<TimeLog> copy = new ArrayList<>(pendingLogs);
            pendingLogs.clear();
          
            return copy;
        }
    }

    public int getPendingCount() {
                      synchronized (pendingLogs) {
            return pendingLogs.size();
        }
    }
}