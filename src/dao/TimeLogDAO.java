package dao;

import db.DBConnection;
import model.TimeLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TimeLogDAO {

    public boolean addTimeLog(TimeLog log) {
        String sql = "INSERT INTO time_logs (user_id, log_date, start_time, end_time, task, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, log.getUserId());
                  ps.setDate(2, Date.valueOf(log.getLogDate()));
             ps.setTime(3, Time.valueOf(log.getStartTime()));
                       ps.setTime(4, Time.valueOf(log.getEndTime()));
             ps.setString(5, log.getTask());
                          ps.setString(6, log.getDescription());

                         return ps.executeUpdate() > 0;

        } catch (SQLException e) {
                        e.printStackTrace();
             return false;
        }
    }

               public List<TimeLog> getLogsByUser(int userId) {
          List<TimeLog> logs = new ArrayList<>();
                String sql = "SELECT * FROM time_logs WHERE user_id = ? ORDER BY log_date DESC";
                         try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

                           ps.setInt(1, userId);
             ResultSet rs = ps.executeQuery();
                      while (rs.next()) logs.add(mapRowToLog(rs));

                 } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

                  public List<TimeLog> getAllLogs() {
                      List<TimeLog> logs = new ArrayList<>();
                           String sql = "SELECT * FROM time_logs ORDER BY log_date DESC";
        try (Connection con = DBConnection.getInstance().getConnection();
                     PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
        
            while (rs.next()) logs.add(mapRowToLog(rs));

        } catch (SQLException e) {
                  e.printStackTrace();
         }
                 return logs;
    }

               public boolean updateTimeLog(TimeLog log) {
                               String sql = "UPDATE time_logs SET log_date=?, start_time=?, end_time=?, task=?, description=? WHERE id=?";
                          try (Connection con = DBConnection.getInstance().getConnection();
                      PreparedStatement ps = con.prepareStatement(sql)) {

                  ps.setDate(1, Date.valueOf(log.getLogDate()));
             ps.setTime(2, Time.valueOf(log.getStartTime()));
                 ps.setTime(3, Time.valueOf(log.getEndTime()));
            ps.setString(4, log.getTask());
          
            ps.setString(5, log.getDescription());
            ps.setInt(6, log.getId());

             return ps.executeUpdate() > 0;

             } catch (SQLException e) {
                     e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTimeLog(int id) {
                   String sql = "DELETE FROM time_logs WHERE id = ?";
        try (Connection con = DBConnection.getInstance().getConnection();
                     PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
                   e.printStackTrace();
            return false;
        }
    }

                public double getTotalHoursByUser(int userId) {
        String sql = "SELECT SUM(TIMESTAMPDIFF(SECOND, start_time, end_time)) / 3600.0 AS total FROM time_logs WHERE user_id = ?";
                   try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

                              ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
       
            if (rs.next()) return rs.getDouble("total");
   
                        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

                 public boolean updateLogWithValidation(TimeLog log) {
        String updateSql = "UPDATE time_logs SET log_date=?, start_time=?, end_time=?, task=?, description=? WHERE id=?";
 
        Connection con = null;
        try {
            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setDate(1, Date.valueOf(log.getLogDate()));
                       ps.setTime(2, Time.valueOf(log.getStartTime()));
           
                             ps.setTime(3, Time.valueOf(log.getEndTime()));
                    ps.setString(4, log.getTask());
                ps.setString(5, log.getDescription());
         
                ps.setInt(6, log.getId());

                int rows = ps.executeUpdate();
                if (rows == 0) {
                           con.rollback();
                    System.out.println("[Transaction] No log updated. Rolled back.");
                    return false;
                }
             }

            con.commit();
                        System.out.println("[Transaction] Log updated. Committed.");
             return true;
 
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
                           e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

                        private TimeLog mapRowToLog(ResultSet rs) throws SQLException {
        return new TimeLog(
                   rs.getInt("id"),
                        rs.getInt("user_id"),
              rs.getDate("log_date").toLocalDate(),
                       rs.getTime("start_time").toLocalTime(),
              
                       rs.getTime("end_time").toLocalTime(),
           
                       rs.getString("task"),
            rs.getString("description")
        );
    }
}