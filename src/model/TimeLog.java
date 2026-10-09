package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeLog {
    private int id;
    private int userId;
    private LocalDate logDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String task;
    private String description;

    public TimeLog() {}

    public TimeLog(int id, int userId, LocalDate logDate, LocalTime startTime,
                   LocalTime endTime, String task, String description) {
        this.id = id;
        this.userId = userId;
        this.logDate = logDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.task = task;
        this.description = description;
    }

    // Convenience method — total hours for  log 
    public double getTotalHours() {
        return (endTime.toSecondOfDay() - startTime.toSecondOfDay()) / 3600.0;
    }

    // Getters & Setters
    public int getId() { return id; }
       public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
      public void setUserId(int userId) { this.userId = userId; }

    public LocalDate getLogDate() { return logDate; }
         public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public LocalTime getStartTime() { return startTime; }
       public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

           public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

            public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }

              public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
   
        return "TimeLog{id=" + id + ", userId=" + userId + ", date=" + logDate +
               ", " + startTime + "-" + endTime + ", task='" + task + "', hours=" + getTotalHours() + "}";
    }
}