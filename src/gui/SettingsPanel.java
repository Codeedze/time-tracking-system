package gui;

import dao.SettingsDAO;
import model.Settings;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private final SettingsDAO settingsDAO = new SettingsDAO();

                       private final JTextField workHoursField = new JTextField(10);
               
               private final JTextField breakTimeField = new JTextField(10);
          private final JLabel statusLabel = new JLabel(" ");

                        public SettingsPanel() {
       
        setLayout(new GridBagLayout());
                  setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

     GridBagConstraints gbc = new GridBagConstraints();
        
     gbc.insets = new Insets(10, 10, 10, 10);
                gbc.anchor = GridBagConstraints.WEST;

  JLabel header = new JLabel("System Settings");

        header.setFont(new Font("Arial", Font.BOLD, 16));
                             gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
   add(header, gbc);
             gbc.gridwidth = 1;

                               gbc.gridx = 0; gbc.gridy = 1;
              add(new JLabel("Work Hours per Day:"), gbc);

     gbc.gridx = 1;
                   add(workHoursField, gbc);

             
                   gbc.gridx = 0; gbc.gridy = 2; 
        add(new JLabel("Break Time (hours):"), gbc);

gbc.gridx = 1;
        add(breakTimeField, gbc);

        JButton saveBtn = new JButton("Save Settings");
                       JButton reloadBtn = new JButton("Reload");


                  
                       JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttons.add(saveBtn);
                    buttons.add(reloadBtn);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
                 add(buttons, gbc);


        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
               
        statusLabel.setForeground(new Color(0, 128, 0));
                     add(statusLabel, gbc);

                         saveBtn.addActionListener(e -> saveSettings());
        reloadBtn.addActionListener(e -> loadSettings());


        loadSettings();
    }

    private void loadSettings() {
                   Settings s = settingsDAO.getSettings();
        workHoursField.setText(String.valueOf(s.getWorkHours()));

                     breakTimeField.setText(String.valueOf(s.getBreakTime()
                    ));
        statusLabel.setText(" ");
    }

    private void saveSettings() {
            try {
            double workHours = Double.parseDouble(workHoursField.getText().trim());

            double breakTime = Double.parseDouble(breakTimeField.getText().trim());

                      if (workHours <= 0 || workHours > 24) {
                throw new NumberFormatException("Work hours must be between 0 and 24.");
            }
                     if (breakTime < 0 || breakTime > workHours) {

                throw new NumberFormatException("Break time must be between 0 and work hours.");
            }
 
                     Settings s = new Settings(1, workHours, breakTime);
            if (settingsDAO.updateSettings(s)) {
                         statusLabel.setForeground(new Color(0, 128, 0));

                statusLabel.setText("✅ Settings saved successfully!");
            } else {
                statusLabel.setForeground(Color.RED);
                     statusLabel.setText("❌ Failed to save settings.");
            }

        } catch (NumberFormatException ex) {
            statusLabel.setForeground(Color.RED);

                       statusLabel.setText("❌ Invalid input: " + ex.getMessage());
        }
    }
}