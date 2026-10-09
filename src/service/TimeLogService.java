package service;

import model.TimeLog;
import exception.InvalidTimeLogException;

import java.util.List;

         public interface TimeLogService {
    boolean addLog(TimeLog log) throws InvalidTimeLogException;
              List<TimeLog> getLogsByUser(int userId);
          List<TimeLog> getAllLogs();
   
   
       boolean updateLog(TimeLog log);
              boolean deleteLog(int id);
    double getTotalHours(int userId);
}