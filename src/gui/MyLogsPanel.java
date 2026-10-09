package gui;

import model.TimeLog;
 import model.User;
import service.TimeLogService;
   import service.TimeLogServiceImpl;

import javax.swing.*;
      import javax.swing.table.DefaultTableModel;
  import java.awt.*;
    import java.time.format.DateTimeFormatter;
      import java.util.List;

public class MyLogsPanel extends JPanel {

    private final User employee;
    
    private final TimeLogService logService = new TimeLogServiceImpl();

             private final DefaultTableModel tableModel;
        private final JTable logTable;
    
        private final JLabel totalHoursLabel = new JLabel("Total Hours: 0.0");

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MyLogsPanel(User employee) {
     
        this.employee = employee;

                    setLayout(new BorderLayout(10, 10));
      
                            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

           // Top label
                  JLabel title = new JLabel("My Time Logs - " + employee.getName());
             title.setFont(new Font("Arial", Font.BOLD, 16));
       
             add(title, BorderLayout.NORTH);

        // Table HERE
                tableModel = new DefaultTableModel(
            
                    new Object[]{"ID", "Date", "Start", "End", "Hours", "Task", "Description"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
              };
        logTable = new JTable(tableModel);
            
               logTable.setRowHeight(24);
         add(new JScrollPane(logTable), BorderLayout.CENTER);

        // Bottom
               JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
       
               JButton refreshBtn = new JButton("Refresh");
        JButton deleteBtn = new JButton("Delete Selected");

        totalHoursLabel.setFont(new Font("Arial", Font.BOLD, 14));

             bottom.add(refreshBtn);
        bottom.add(deleteBtn);
                        bottom.add(Box.createHorizontalStrut(30));
        bottom.add(totalHoursLabel);

                   add(bottom, BorderLayout.SOUTH);
 
        refreshBtn.addActionListener(e -> loadLogs());
                   deleteBtn.addActionListener(e -> deleteSelected());

                   loadLogs();
    }

      private void loadLogs() {
            tableModel.setRowCount(0);
       List<TimeLog> logs = logService.getLogsByUser(employee.getId());

        for (TimeLog log : logs) {
     tableModel.addRow(new Object[]{
                log.getId(),
           
                    log.getLogDate().format(DATE_FMT),
           
                log.getStartTime().toString(),
                   log.getEndTime().toString(),
                        String.format("%.2f", log.getTotalHours()),
           log.getTask(),
                log.getDescription()
            });
        }

                   double total = logService.getTotalHours(employee.getId());
    
                   totalHoursLabel.setText(String.format("Total Hours: %.2f", total));
    }

    private void deleteSelected() {
        int row = logTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a log to delete.");
            return;
        }

        int logId = (int) tableModel.getValueAt(row, 0);
     
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete this log entry?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (logService.deleteLog(logId)) {
                 
                JOptionPane.showMessageDialog(this, "Log deleted.");
                loadLogs();
            }
        }
    }
        public void refresh() {
        loadLogs();
    }
    
}