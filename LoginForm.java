import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {
    JTextField emailField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginForm() {
        setTitle("User Login");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 30, 100, 20);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 30, 150, 20);
        add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 70, 100, 20);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 70, 150, 20);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(100, 110, 100, 30);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(
                            "SELECT * FROM users WHERE email=? AND password=?"
                    );
                    pst.setString(1, emailField.getText());
                    pst.setString(2, String.valueOf(passwordField.getPassword()));

                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Login Successful!");
                        new ToDoApp(rs.getInt("id")).setVisible(true);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Credentials!");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
