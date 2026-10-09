package gui;

    import model.User;
       import service.UserService;
  import service.UserServiceImpl;
        import exception.InvalidLoginException;

  import javax.swing.*;
     import java.awt.*;

public class LoginFrame extends JFrame {

             private final JTextField emailField = new JTextField(20);
        private final JPasswordField passwordField = new JPasswordField(20);
           
        private final UserService userService = new UserServiceImpl();

    public LoginFrame() {
      
        setTitle("Time Tracking System - Login");
       
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
     setSize(400, 250);
             
             setLocationRelativeTo(null);
                      setLayout(new BorderLayout());

        JLabel header = new JLabel("Time Tracking System", SwingConstants.CENTER);
                 
        header.setFont(new Font("Arial", Font.BOLD, 20));
                             
                    header.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
      
                           add(header, BorderLayout.NORTH);

             JPanel form = new JPanel(new GridBagLayout());
              GridBagConstraints gbc = new GridBagConstraints();
     gbc.insets = new Insets(8, 8, 8, 8);
              
         gbc.anchor = GridBagConstraints.WEST;

 gbc.gridx = 0; gbc.gridy = 0;
       
    form.add(new JLabel("Email:"), gbc);
             gbc.gridx = 1;
                   form.add(emailField, gbc);

           gbc.gridx = 0; gbc.gridy = 1;
                          form.add(new JLabel("Password:"), gbc);
         
         
                          gbc.gridx = 1;
        form.add(passwordField, gbc);

        add(form, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
              
        JButton loginBtn = new JButton("Login");
             JButton exitBtn = new JButton("Exit");
          buttons.add(loginBtn);
        buttons.add(exitBtn);
       
        add(buttons, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> handleLogin());
                exitBtn.addActionListener(e -> System.exit(0));
 
        getRootPane().setDefaultButton(loginBtn);
    }

 private void handleLogin() {
        String email = emailField.getText().trim();
           String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password.",
                    "Missing input", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
                    User user = userService.login(email, password);
            JOptionPane.showMessageDialog(this, "Welcome, " + user.getName() + "!");

            // Route based on role (POLYMORPHISM in action)
                   if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                            new AdminDashboard(user).setVisible(true);
            } else {
                         new EmployeeDashboard(user).setVisible(true);
            }
                   dispose();

        } catch (InvalidLoginException ex) {
                       JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Login failed", JOptionPane.ERROR_MESSAGE);
        }
    }

                 public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}