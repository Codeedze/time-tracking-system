package gui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private final User admin;
       
  private final AllLogsPanel allLogsPanel;
      
  private final ReportPanel reportPanel;

    public AdminDashboard(User admin) {
        this.admin = admin;

        setTitle("Admin Dashboard - " + admin.getName());
           
          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1050, 650);
        
            setLocationRelativeTo(null);

                   allLogsPanel = new AllLogsPanel();
        reportPanel = new ReportPanel();

        JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("Users", new UserManagementPanel());
                
            tabs.addTab("Time Logs", allLogsPanel);
         
            tabs.addTab("Reports", reportPanel);
        tabs.addTab("Settings", new SettingsPanel());   // ← changed

        tabs.addChangeListener(e -> {
                    int idx = tabs.getSelectedIndex();
                 if (idx == 1) allLogsPanel.refresh();
                   if (idx == 2) reportPanel.refresh();
        });

        add(tabs, BorderLayout.CENTER);

             JLabel status = new JLabel(" Logged in as: " + admin.getName() + " (ADMIN)");
        
        
             status.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(status, BorderLayout.SOUTH);
    }
}