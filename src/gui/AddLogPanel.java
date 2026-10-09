package gui;

import model.TimeLog;
import model.User;
import service.TimeLogService;
import service.TimeLogServiceImpl;
import exception.InvalidTimeLogException;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AddLogPanel extends JPanel {

    private final User employee;
      private final TimeLogService logService = new TimeLogServiceImpl();

    private final JTextField dateField = new JTextField(10);
   
 private final JTextField startField = new JTextField(10);
             private final JTextField endField = new JTextField(10);
                        private final JTextField taskField = new JTextField(20);
                    
     private final JTextArea descArea = new JTextArea(4, 20);

                  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

                private Runnable onSaveCallback; // to refresh logs list after adding

      public AddLogPanel(User employee) {
        this.employee = employee;

        setLayout(new GridBagLayout());
              setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
      
        gbc.insets = new Insets(6, 6, 6, 6);
                gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pre-fill date and reasonabletime
               dateField.setText(LocalDate.now().format(DATE_FMT));
             
               startField.setText("09:00");
        endField.setText("17:00");

        int row = 0;

        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Date (yyyy-MM-dd):"), gbc);
       
             gbc.gridx = 1; add(dateField, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Start Time (HH:mm):"), gbc);
                
        gbc.gridx = 1; add(startField, gbc); row++;

        
                gbc.gridx = 0; gbc.gridy = row; add(new JLabel("End Time (HH:mm):"), gbc);
               gbc.gridx = 1; add(endField, gbc); row++;

         
               gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Task:"), gbc);
        gbc.gridx = 1; add(taskField, gbc); row++;

        gbc.gridx = 0; gbc.gridy = row; add(new JLabel("Description:"), gbc);
        
        
        gbc.gridx = 1;
        
               gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(descArea), gbc);
        row++;

        // Buttons
                      JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
           JButton saveBtn = new JButton("Save Log");
      JButton clearBtn = new JButton("Clear");
                     buttons.add(saveBtn);
      buttons.add(clearBtn);

                         gbc.gridx = 0; gbc.gridy = row;
                gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(buttons, gbc);

            saveBtn.addActionListener(e -> saveLog());
                   clearBtn.addActionListener(e -> clearForm());
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    private void saveLog() {
        try {
                   LocalDate date = LocalDate.parse(dateField.getText().trim(), DATE_FMT);
              LocalTime start = LocalTime.parse(startField.getText().trim(), TIME_FMT);
                
            LocalTime end = LocalTime.parse(endField.getText().trim(), TIME_FMT);
            
            String task = taskField.getText().trim();
             String desc = descArea.getText().trim();

                TimeLog log = new TimeLog(0, employee.getId(), date, start, end, task, desc);
             logService.addLog(log); // throws InvalidTimeLogException if invalid

                 JOptionPane.showMessageDialog(this, "Log saved successfully!");
            clearForm();
            
            if (onSaveCallback != null) onSaveCallback.run();

        } catch
         (java.time.format.DateTimeParseException ex) {
         
            JOptionPane.showMessageDialog(this,
              
                "Invalid date or time format.\nUse yyyy-MM-dd and HH:mm",
                "Format Error", JOptionPane.ERROR_MESSAGE);
        } 
        catch (InvalidTimeLogException ex) {
            
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                "Validation", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearForm() {
           dateField.setText(LocalDate.now().format(DATE_FMT));
      
           startField.setText("09:00");
      endField.setText("17:00");
      
      taskField.setText("");
       
      descArea.setText("");
    }
}