package test;

import model.*;
import service.*;
import thread.*;
import util.TimeLogManager;

import java.time.LocalDate;
import java.time.LocalTime;

public class TestService {
    public static void main(String[] args) throws Exception {

        UserService userService = new UserServiceImpl();

        // Login test
                  User admin = userService.login("admin@tts.com", "admin123");
        System.out.println("Logged in as: " + admin);

        // Auto-save thread starts in the background
                  AutoSaveThread autoSave = new AutoSaveThread();
        autoSave.start();

        // Simulate rapid log entries from a GUI
        TimeLogManager manager = TimeLogManager.getInstance();
                    for (int i = 1; i <= 3; i++) {
            manager.addPendingLog(new TimeLog(0, admin.getId(),
                LocalDate.now(),
           
                     LocalTime.of(9, 0),
                LocalTime.of(10, 0),
                "Task-" + i,
                         "Pending entry " + i));
        }
        System.out.println("Pending count: " + manager.getPendingCount());

        // Report thread
                   ReportThread reportThread = new ReportThread(admin.getId());
        reportThread.start();
                     while (!reportThread.isDone()) {
            Thread.sleep(100);
        }
        System.out.println("Report total: " + reportThread.getResult());

        // Let autosave run once (wait ~31 s), or shutdown manually
        Thread.sleep(31_000);
                autoSave.shutdown();

        System.out.println("TestService done.");
    }
}