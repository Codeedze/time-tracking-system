package dao;

import db.DBConnection;
import model.Settings;

import java.sql.*;

public class SettingsDAO {

                  public Settings getSettings() {
         String sql = "SELECT * FROM settings WHERE id = 1";
        
         try (Connection con = DBConnection.getInstance().getConnection();
               PreparedStatement ps = con.prepareStatement(sql);
        
               ResultSet rs = ps.executeQuery()) {

                        if (rs.next()) {
                return new Settings(
                    rs.getInt("id"),
                         rs.getDouble("work_hours"),
                      rs.getDouble("break_time")
                );
            }

            } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Settings(1, 8.0, 1.0); // default fallback
    }

    public boolean updateSettings(Settings s) {
                              String sql = "UPDATE settings SET work_hours = ?, break_time = ? WHERE id = 1";
          try (Connection con = DBConnection.getInstance().getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, s.getWorkHours());
                  ps.setDouble(2, s.getBreakTime());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}