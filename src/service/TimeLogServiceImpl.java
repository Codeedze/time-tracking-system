package service;

import dao.TimeLogDAO;
 import model.TimeLog;
import exception.InvalidTimeLogException;

import java.util.List;

public class TimeLogServiceImpl implements TimeLogService {

    private final TimeLogDAO logDAO = new TimeLogDAO();

    @Override
    public boolean addLog(TimeLog log) throws InvalidTimeLogException {
       
        if (log.getEndTime().isBefore(log.getStartTime()) ||
                   
        log.getEndTime().equals(log.getStartTime())) {
         
                        throw new InvalidTimeLogException("End time must be after start time.");
        }
        if (log.getTask() == null || log.getTask().trim().isEmpty()) {
               
          
            throw new InvalidTimeLogException("Task name cannot be empty.");
        }
        return logDAO.addTimeLog(log);
    }

    @Override
               
    public List<TimeLog> getLogsByUser(int userId) {
        return logDAO.getLogsByUser(userId);
    }

    @Override
           public List<TimeLog> getAllLogs() {
                 return logDAO.getAllLogs();
    }

    @Override
              public boolean updateLog(TimeLog log) {
        return logDAO.updateTimeLog(log);
    }

    @Override
            
    public boolean deleteLog(int id) {
       
        return logDAO.deleteTimeLog(id);
    }

    @Override
    public double getTotalHours(int userId) {
        return logDAO.getTotalHoursByUser(userId);
    }
}