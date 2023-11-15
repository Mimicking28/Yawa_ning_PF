import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;

public class Homepage implements ActionListener {

    JFrame frame;
    JLabel titleLabel = new JLabel("Online Hotel Reservation");
    JLabel nameLabel = new JLabel("Name:");
    JTextField nameTextField = new JTextField();
    JLabel addressLabel = new JLabel("Address:");
    JTextField addressTextField = new JTextField();
    JLabel emailLabel = new JLabel("Email:");
    JTextField emailTextField = new JTextField();
    JLabel roomLabel = new JLabel("Room Type:");
    String[] types = { "Single", "Double", "Family", "Duplex", "Presidential Suite" };
    JComboBox<String> roomTextField = new JComboBox<>(types);
    JLabel guestLabel = new JLabel("No. of Guests:");
    JTextField guestTextField = new JTextField();
    JLabel checkInLabel = new JLabel("Date of Check-in:");
    JTextField checkInTextField = new JTextField("yy/mm/dd");
    JLabel checkOutLabel = new JLabel("Date of Check-out:");
    JTextField checkoutFieldText = new JTextField("yy/mm/dd");
    JButton submitButton = new JButton("Submit");
    JButton cancelButton = new JButton("Cancel");

    Homepage() {
        createWindow();
        setLocationAndSize();
        addComponentsToFrame();
    }

    public void createWindow() {

        frame.setTitle("Online Hotel Reservation");
        frame.setBounds(650, 250, 600, 800);
        frame.getContentPane().setBackground(Color.pink);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    public void setLocationAndSize() {
        titleLabel.setBounds(150, 25, 300, 20);
        nameLabel.setBounds(50, 60, 100, 30);
        nameTextField.setBounds(170, 60, 300, 25);
        addressLabel.setBounds(50, 100, 100, 30);
        addressTextField.setBounds(170, 100, 300, 25);
        emailLabel.setBounds(50, 140, 100, 30);
        emailTextField.setBounds(170, 140, 300, 25);
        roomLabel.setBounds(50, 180, 100, 30);
        roomTextField.setBounds(170, 180, 150, 25);
        guestLabel.setBounds(50, 220, 100, 30);
        guestTextField.setBounds(170, 220, 100, 25);
        checkInLabel.setBounds(50, 260, 150, 30);
        checkInTextField.setBounds(170, 260, 100, 25);
        checkOutLabel.setBounds(50, 300, 150, 30);
        checkoutFieldText.setBounds(170, 300, 100, 25);
        submitButton.setBounds(150, 350, 100, 30);
        cancelButton.setBounds(280, 350, 100, 30);
        submitButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    public void addComponentsToFrame() {
        frame.add(titleLabel);
        frame.add(nameLabel);
        frame.add(nameTextField);
        frame.add(addressLabel);
        frame.add(addressTextField);
        frame.add(emailLabel);
        frame.add(emailTextField);
        frame.add(roomLabel);
        frame.add(roomTextField);
        frame.add(guestLabel);
        frame.add(guestTextField);
        frame.add(checkInLabel);
        frame.add(checkInTextField);
        frame.add(checkOutLabel);
        frame.add(checkoutFieldText);
        frame.add(submitButton);
        frame.add(cancelButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {

            String name = nameTextField.getText();
            String address = addressTextField.getText();
            String email = emailTextField.getText();
            String roomType = (String) roomTextField.getSelectedItem();
            String guests = guestTextField.getText();
            String checkInDate = checkInTextField.getText();
            String checkOutDate = checkoutFieldText.getText();

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost/pf", "root", "");
                Statement statement = con.createStatement();

                String query = String.format(
                        "INSERT INTO reservations (name, address, email, room_type, guests, check_in, check_out) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                        name, address, email, roomType, guests, checkInDate, checkOutDate);

                int rowsAffected = statement.executeUpdate(query);

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Reservation submitted successfully!");
                    frame.dispose();
                    new DataDisplayFrame();

                } else {
                    JOptionPane.showMessageDialog(frame, "Error submitting reservation. Please try again.");
                }

                statement.close();
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error connecting to the database. Please check your connection.");
            } catch (ClassNotFoundException e1) {

                e1.printStackTrace();
            }
        }
        if (e.getSource() == cancelButton) {
            frame.dispose();

        }
    }

    public void setVisible(boolean b) {
    }

}