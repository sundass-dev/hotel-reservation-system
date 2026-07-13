import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistrationForm extends JFrame {
    JTextField nameField, emailField;
    JPasswordField passwordField;
    JButton registerButton;

    public RegistrationForm() {
        setTitle("User Registration");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 20);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(120, 20, 150, 20);
        add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 50, 100, 20);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(120, 50, 150, 20);
        add(emailField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(20, 80, 100, 20);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(120, 80, 150, 20);
        add(passwordField);

        registerButton = new JButton("Register");
        registerButton.setBounds(100, 120, 100, 30);
        add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement pst = con.prepareStatement(
                            "INSERT INTO users(name, email, password) VALUES(?,?,?)"
                    );
                    pst.setString(1, nameField.getText());
                    pst.setString(2, emailField.getText());
                    pst.setString(3, String.valueOf(passwordField.getPassword()));
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(null, "User Registered Successfully!");
                    new LoginForm().setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        new RegistrationForm().setVisible(true);
    }
}
