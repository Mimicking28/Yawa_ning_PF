import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Login implements ActionListener {

    JFrame frame;
    JLabel labelExtraHeader;
    JLabel labelUsername;
    JTextField textFieldUsername;
    JLabel labelPassword;
    JPasswordField passwordField;
    JButton loginButton;

    public Login() {
        frame = new JFrame("Login Page");
        labelExtraHeader = new JLabel("Welcome to Our Application");
        labelUsername = new JLabel("Username:");
        textFieldUsername = new JTextField();
        labelPassword = new JLabel("Password:");
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");

        createWindow();
        setLocationAndSize();
        addComponentsToFrame();
    }

    public void createWindow() {
        frame.setTitle("Administrator Log In Form");
        frame.setBounds(650, 250, 600, 400);
        frame.getContentPane().setBackground(Color.pink);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    public void setLocationAndSize() {
        labelExtraHeader.setBounds(50, 60, 300, 50);
        labelUsername.setBounds(50, 120, 100, 30);
        textFieldUsername.setBounds(170, 120, 300, 30);
        labelPassword.setBounds(50, 170, 100, 30);
        passwordField.setBounds(170, 170, 300, 30);
        loginButton.setBounds(250, 240, 100, 30);
        loginButton.addActionListener(this);
    }

    public void addComponentsToFrame() {
        frame.add(labelExtraHeader);
        frame.add(labelUsername);
        frame.add(textFieldUsername);
        frame.add(labelPassword);
        frame.add(passwordField);
        frame.add(loginButton);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = textFieldUsername.getText();
            String password = new String(passwordField.getPassword());
            if (username.equals("admin") && password.equals("admin123")) {
                JOptionPane.showMessageDialog(frame, "Login successful");
                frame.dispose();
                new DataDisplayFrame();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials");
            }
        }
    }

}