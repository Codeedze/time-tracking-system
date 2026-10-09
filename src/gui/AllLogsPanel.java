package gui;

import model.TimeLog;

import service.TimeLogService;

import service.TimeLogServiceImpl;

        import javax.swing.*;
     import javax.swing.table.DefaultTableModel;
import java.awt.*;

import java.time.format.DateTimeFormatter;

      import java.util.List;

public class AllLogsPanel extends JPanel {

          private final TimeLogService logService = new TimeLogServiceImpl();

             private final DefaultTableModel tableModel;
     private final JTable logTable;
                      private final JLabel summaryLabel = new JLabel("Total logs: 0 | Total hours: 0.00");

            private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                       public AllLogsPanel() {
           setLayout(new BorderLayout(10, 10));
       
           setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

           JLabel title = new JLabel("All Time Logs (All Users)");
                   title.setFont(new Font("Arial", Font.BOLD, 16));
         
                   add(title, BorderLayout.NORTH);

                       tableModel = new DefaultTableModel(
             new Object[]{"ID", "User ID", "Date", "Start", "End", "Hours", "Task", "Description"}, 0) {
                         
                @Override public boolean isCellEditable(int r, int c) { return false; }
        };
                        logTable = new JTable(tableModel);
                   logTable.setRowHeight(24);
        add(new JScrollPane(logTable), BorderLayout.CENTER);
 
                JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
      
                JButton refreshBtn = new JButton("Refresh");
     
                           JButton deleteBtn = new JButton("Delete Selected");
            summaryLabel.setFont(new Font("Arial", Font.BOLD, 13));

             bottom.add(refreshBtn);
                    bottom.add(deleteBtn);
               bottom.add(Box.createHorizontalStrut(30));
        
               bottom.add(summaryLabel);
       
               add(bottom, BorderLayout.SOUTH);

                    refreshBtn.addActionListener(e -> loadLogs());
        deleteBtn.addActionListener(e -> deleteSelected());

        loadLogs();
    }

    private void loadLogs() {
        tableModel.setRowCount(0);
        List<TimeLog> logs = logService.getAllLogs();
        double grandTotal = 0.0;

        for (TimeLog log : logs) {
            grandTotal += log.getTotalHours();
            tableModel.addRow(new Object[]{
                log.getId(), 
                log.getUserId(),

                log.getLogDate().format(DATE_FMT),

                
                log.getStartTime().toString(),
                
                log.getEndTime().toString(),
                      String.format("%.2f", log.getTotalHours()),
                           log.getTask(),
                   log.getDescription()
            });
        }

            summaryLabel.setText(String.format("Total logs: %d | Total hours: %.2f", logs.size(), grandTotal));
    }

    public void refresh() { loadLogs(); }

    private void deleteSelected() {
              int row = logTable.getSelectedRow();
                      if (row < 0)
                         {
            JOptionPane.showMessageDialog(this, "Select a log to delete.");
            return;}
        
                  int logId = (int) tableModel.getValueAt(row, 0);
                         int confirm = JOptionPane.showConfirmDialog(this,
                "Delete log #" + logId + "?", "Confirm", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION && logService.deleteLog(logId)) {
            loadLogs();
        }
    }
}