package db;

import java.sql.Connection;
          import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

           private static final String URL = "jdbc:mariadb://localhost:3306/time_tracking_db";
   
   
            private static final String USER = "root";
              
     private static final String PASSWORD = "databasekshitiz";

                         private static DBConnection instance;

    private DBConnection() {
          
        try {
        
        
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MariaDB Driver not found", e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
          
          
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
                     return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}