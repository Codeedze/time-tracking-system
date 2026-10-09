package gui;

import model.Admin;
import model.Employee;

import model.User; 
import service.UserService;

import service.UserServiceImpl; 

import exception.DuplicateUserException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

public class UserManagementPanel extends JPanel {

              private final UserService userService = new UserServiceImpl();

                     private final DefaultTableModel tableModel;
    private final JTable userTable;

       private final JTextField nameField = new JTextField(15);

    
          private final JTextField emailField = new JTextField(15);
    private final JPasswordField passwordField = new JPasswordField(15);
    
                     private final JComboBox<String> roleBox = new JComboBox<>(new String[]{"EMPLOYEE", "ADMIN"});

            private int selectedUserId = -1; // -1 = none selected

    public UserManagementPanel() {
                 setLayout(new BorderLayout(10, 10));

                             setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top: form 
       
        JPanel form = new JPanel(new GridBagLayout());
      
      
         form.setBorder(BorderFactory.createTitledBorder("User Details"));
        GridBagConstraints gbc = new GridBagConstraints();


                            gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;



        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; form.add(nameField, gbc);


        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1; form.add(emailField, gbc);

                    gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Password:"), gbc);
       
                  gbc.gridx = 1; form.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; form.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1; form.add(roleBox, gbc);

        add(form, BorderLayout.NORTH);

        // ---------- Center: table ----------
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Role"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            
        };
        userTable = new JTable(tableModel);
              userTable.setRowHeight(24);   
                  userTable.getSelectionModel().addListSelectionListener(e -> onRowSelected());

        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // ---------- Bottom: buttons ----------
                    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
          JButton addBtn = new JButton("Add");
                 
        
          JButton updateBtn = new JButton("Update");
JButton deleteBtn = new JButton("Delete");
        
JButton clearBtn = new JButton("Clear Form");
        JButton refreshBtn = new JButton("Refresh");

        buttons.add(addBtn);
            buttons.add(updateBtn);
                
            buttons.add(deleteBtn);
                        buttons.add(clearBtn);
           buttons.add(refreshBtn);

        add(buttons, BorderLayout.SOUTH);

        // ---------- Actions ----------
                      addBtn.addActionListener(e -> addUser());
       
                      updateBtn.addActionListener(e -> updateUser());
               deleteBtn.addActionListener(e -> deleteUser());
      
            
               clearBtn.addActionListener(e -> clearForm());
       
               refreshBtn.addActionListener(e -> loadUsers());

        loadUsers();
    }

    // ---------- Business logic ----------

    private void loadUsers() {
        tableModel.setRowCount(0);
        List<User> users = userService.getAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{u.getId(), u.getName(), u.getEmail(), u.getRole()});
        }
    }

    private void onRowSelected() {
        int row = userTable.getSelectedRow();
        if (row < 0) return;

        selectedUserId = (int) tableModel.getValueAt(row, 0);
                  
               nameField.setText((String) tableModel.getValueAt(row, 1));
         
      emailField.setText((String) tableModel.getValueAt(row, 2));
                           
        
                   roleBox.setSelectedItem(tableModel.getValueAt(row, 3));
        passwordField.setText(""); // don't show old password
    }

    private void addUser() {
        if (!validateForm()) return;

        User user = roleBox.getSelectedItem().equals("ADMIN")
             
       
        ? new Admin(0, nameField.getText().trim(), emailField.getText().trim(),
                               new String(passwordField.getPassword()))
             
                   
                               : new Employee(0, nameField.getText().trim(), emailField.getText().trim(),
             new String(passwordField.getPassword()));

        try {
            if (userService.registerUser(user)) {
                
                JOptionPane.showMessageDialog(this, "User added successfully!");
                      clearForm();
           
                      loadUsers();
            }
        } catch (DuplicateUserException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Duplicate user", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() {
      
        if (selectedUserId == -1) {
           
            JOptionPane.showMessageDialog(this, "Please select a user from the table first.");
            return;
        }
        if (!validateForm()) return;

        User user = roleBox.getSelectedItem().equals("ADMIN")
              
        ? new Admin(selectedUserId, nameField.getText().trim(),
                            emailField.getText().trim(),
          
          
         new String(passwordField.getPassword()))
                                           
                : new Employee(selectedUserId, nameField.getText().trim(),
                               emailField.getText().trim(),
                               new String(passwordField.getPassword()));

        if (userService.updateUser(user)) {
            JOptionPane.showMessageDialog(this, "User updated successfully!");
          
            clearForm();
            loadUsers();
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser() {
        if (selectedUserId == -1) {
          
            JOptionPane.showMessageDialog(this, "Please select a user from the table first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
               
            
            "Are you sure you want to delete this user?\nTheir time logs will also be deleted.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
                     if (userService.deleteUserWithLogs(selectedUserId)) {
              
                        JOptionPane.showMessageDialog(this, "User deleted.");
             
                clearForm();
                loadUsers();
            }
        }
    }

    private boolean validateForm() {
        if (nameField.getText().trim().isEmpty() ||
          
        
        emailField.getText().trim().isEmpty() ||
                    passwordField.getPassword().length == 0) {
                         
                        JOptionPane.showMessageDialog(this, "All fields are required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!emailField.getText().contains("@")) {
                         
            JOptionPane.showMessageDialog(this, "Please enter a valid email.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearForm() {
          nameField.setText("");
                  emailField.setText("");
        
                  passwordField.setText("");
           
                  roleBox.setSelectedIndex(0);
  selectedUserId = -1;
        userTable.clearSelection();
    }
}