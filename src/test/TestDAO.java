package test;

import dao.*;
import model.*;
import exception.*;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

public class TestDAO {
    public static void main(String[] args) {
        try {
            UserDAO userDAO = new UserDAO();

            // 1. CREATE - add an employee
            Employee emp = new Employee(0, "TestUser", "test@tts.com", "test123");
            System.out.println("Add user: " + userDAO.addUser(emp));

            // 2. READ - get by email
            User fetched = userDAO.getUserByEmail("test@tts.com");
            System.out.println("Fetched: " + fetched);

            // 3. READ - all
            List<User> all = userDAO.getAllUsers();
            System.out.println("Total users: " + all.size());

            // 4. TimeLog - add
            TimeLogDAO logDAO = new TimeLogDAO();
            TimeLog log = new TimeLog(0, fetched.getId(),
                LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(17, 30),
                "Coding", "Worked on the TTS project");
            System.out.println("Add log: " + logDAO.addTimeLog(log));

            // 5. Read logs
            List<TimeLog> logs = logDAO.getLogsByUser(fetched.getId());
            System.out.println("Logs for user: " + logs);

            // 6. Aggregate
            System.out.println("Total hours: " + logDAO.getTotalHoursByUser(fetched.getId()));

            // 7. Settings
            SettingsDAO settingsDAO = new SettingsDAO();
            System.out.println("Settings: " + settingsDAO.getSettings());

            // 8. UPDATE
            fetched.setName("TestUser Updated");
            System.out.println("Update user: " + userDAO.updateUser(fetched));

            // 9. DELETE (optional — comment out if you want to keep the test data)
            // System.out.println("Delete user: " + userDAO.deleteUser(fetched.getId()));

        } catch (DuplicateUserException | UserNotFoundException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}