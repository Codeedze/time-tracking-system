package gui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class EmployeeDashboard extends JFrame {

              private final User employee;
                  private final MyLogsPanel myLogsPanel;
    private final ReportPanel reportPanel;

          public EmployeeDashboard(User employee) {
  
            this.employee = employee;

           setTitle("Employee Dashboard - " + employee.getName());
     
           setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
                setSize(1000, 620);
        setLocationRelativeTo(null);

        myLogsPanel = new MyLogsPanel(employee);
      
                AddLogPanel addLogPanel = new AddLogPanel(employee);
      
                reportPanel = new ReportPanel();

        addLogPanel.setOnSaveCallback(() -> myLogsPanel.refresh());

        JTabbedPane tabs = new JTabbedPane();
                
                   tabs.addTab("My Logs", myLogsPanel);
           
                   tabs.addTab("Add Log", addLogPanel);
        tabs.addTab("Reports", reportPanel);

                         tabs.addChangeListener(e -> {
            int idx = tabs.getSelectedIndex();
                     if (idx == 0) myLogsPanel.refresh();
              if (idx == 2) reportPanel.refresh();
        });

                       add(tabs, BorderLayout.CENTER);

                  JLabel status = new JLabel(" Logged in as: " + employee.getName() + " (EMPLOYEE)");
        
                  status.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(status, BorderLayout.SOUTH);
    }
}