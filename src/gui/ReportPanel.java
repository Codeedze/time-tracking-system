package gui;

import dao.TimeLogDAO;
import model.TimeLog;
      import model.User;
   import service.UserService;
 import service.UserServiceImpl;

       import org.jfree.chart.ChartFactory;
   import org.jfree.chart.ChartPanel;

  import org.jfree.chart.JFreeChart;

     import org.jfree.chart.plot.PlotOrientation;
  import org.jfree.data.category.DefaultCategoryDataset;
      import org.jfree.data.general.DefaultPieDataset;

  import javax.swing.*;
   import java.awt.*;

      import java.util.HashMap;
 import java.util.List;
    import java.util.Map;

public class ReportPanel extends JPanel {

         private final UserService userService = new UserServiceImpl();
            private final TimeLogDAO logDAO = new TimeLogDAO();

                 private final JPanel chartHolder = new JPanel(new BorderLayout());
 private JComboBox<String> chartTypeBox;

          public ReportPanel() {
     
            setLayout(new BorderLayout(10, 10));
      
      setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                    top.add(new JLabel("Chart Type:"));
    
                    chartTypeBox = new JComboBox<>(new String[]{
            "Hours per User (Bar)",
                
                   "Log Count per User (Bar)",
            "Hours Share per User (Pie)"
        });
    
    
        top.add(chartTypeBox);

        JButton refreshBtn = new JButton("Refresh");
                top.add(refreshBtn);

          
                add(top, BorderLayout.NORTH);
        add(chartHolder, BorderLayout.CENTER);

                   chartTypeBox.addActionListener(e -> refresh());
        refreshBtn.addActionListener(e -> refresh());

        refresh();
    }

                  public void refresh() {
       
                    String type = (String) chartTypeBox.getSelectedItem();
        JFreeChart chart;
 
                    if (type.startsWith("Hours per User")) {
      
                        chart = buildBarChart("Total Hours per User", "Hours");
                          } else if (type.startsWith("Log Count")) {
            chart = buildLogCountChart();
        } else {
       
       
            chart = buildPieChart();
        }

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 450));

                chartHolder.removeAll();
     chartHolder.add(chartPanel, BorderLayout.CENTER);
               
              chartHolder.revalidate();
      
              chartHolder.repaint();
    }

                private DefaultCategoryDataset buildHoursDataset() {
              
                    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<User> users = userService.getAllUsers();
 for (User u : users) {
            double hours = logDAO.getTotalHoursByUser(u.getId());
            dataset.addValue(hours, "Hours", u.getName());
        }
        return dataset;
    }

                 private JFreeChart buildBarChart(String title, String yLabel) {
   DefaultCategoryDataset dataset = buildHoursDataset();
         
   return ChartFactory.createBarChart(
              
    title, "User", yLabel, dataset,
                PlotOrientation.VERTICAL, false, true, false);
    }

    private JFreeChart buildLogCountChart() {
          
             DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    
             List<User> users = userService.getAllUsers();
     
             List<TimeLog> allLogs = logDAO.getAllLogs();

        Map<Integer, Integer> counts = new HashMap<>();
        
         for (TimeLog log : allLogs) {
         
         
            counts.merge(log.getUserId(), 1, Integer::sum);
        }

        for (User u : users) {
       
            int count = counts.getOrDefault(u.getId(), 0);
                      dataset.addValue(count, "Logs", u.getName());
        }
        return ChartFactory.createBarChart(
                   
                    "Number of Logs per User", "User", "Log Count", dataset,
                PlotOrientation.VERTICAL, false, true, false);
    }

    private JFreeChart buildPieChart() {
                        
                     DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        List<User> users = userService.getAllUsers();
            for (User u : users) {
          
                double hours = logDAO.getTotalHoursByUser(u.getId());
            if (hours > 0) {
                dataset.setValue(u.getName(), hours);
            }
        }
        return ChartFactory.createPieChart(
                "Hours Share per User", dataset, true, true, false);
    }
}