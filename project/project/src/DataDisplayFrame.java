import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DataDisplayFrame {

    private JFrame frame;
    private JTextArea displayTextArea;

    public DataDisplayFrame() {
        initialize();
        fetchDataFromDatabase();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame("Data Display and Management");
        frame.setBounds(200, 200, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        displayTextArea = new JTextArea();
        displayTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(displayTextArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Data");
        JButton editButton = new JButton("Edit Selected Data");
        JButton deleteButton = new JButton("Delete Selected Data");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAddAction();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleEditAction();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDeleteAction();
            }
        });
    }

    private void fetchDataFromDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/pf";
            String username = "root";
            String password = "";

            try (Connection con = DriverManager.getConnection(url, username, password);
                    Statement statement = con.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM reservations")) {

                StringBuilder data = new StringBuilder();
                while (resultSet.next()) {

                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String room_type = resultSet.getString("room_type");
                    String check_in = resultSet.getString("check_in");
                    String check_out = resultSet.getString("check_out");

                    data.append("Name: ").append(name).append("\n");
                    data.append("Email: ").append(email).append("\n");
                    data.append("Room Type: ").append(room_type).append("\n");
                    data.append("Check in Date: ").append(check_in).append("\n");
                    data.append("Check out Date: ").append(check_out).append("\n");
                    data.append(
                            "_____________________________________________________________________________________________________");
                    data.append("\n");

                }

                displayTextArea.setText(data.toString());

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error fetching data from the database: " + ex.getMessage());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Class Not Found Error: " + e.getMessage());
        }
    }

    private void handleAddAction() {
        SwingUtilities.invokeLater(() -> {
            Homepage homepage = new Homepage();
            homepage.setVisible(true);
        });
        frame.dispose();
    }

    private void handleEditAction() {
        String selectedData = displayTextArea.getSelectedText();
        if (selectedData != null && !selectedData.isEmpty()) {

            String editedData = showEditDialog(selectedData);

            if (editedData != null) {

                String currentText = displayTextArea.getText();
                currentText = currentText.replace(selectedData, editedData);
                displayTextArea.setText(currentText);

                updateDataInDatabase(selectedData, editedData);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No data selected for editing.");
        }
    }

    private void handleDeleteAction() {
        String selectedData = displayTextArea.getSelectedText();
        if (selectedData != null && !selectedData.isEmpty()) {

            int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this data?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {

                String currentText = displayTextArea.getText();
                currentText = currentText.replace(selectedData, "");
                displayTextArea.setText(currentText);

                deleteDataFromDatabase(selectedData);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No data selected for deletion.");
        }
    }

    private String showEditDialog(String selectedData) {
        JTextArea textArea = new JTextArea(selectedData);
        JScrollPane scrollPane = new JScrollPane(textArea);

        int option = JOptionPane.showOptionDialog(frame, scrollPane, "Edit Data", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            return textArea.getText();
        } else {
            return null;
        }
    }

    private void updateDataInDatabase(String selectedData, String editedData) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/pf";
            String username = "root";
            String password = "";

            try (Connection con = DriverManager.getConnection(url, username, password);
                    Statement statement = con.createStatement()) {

                String[] oldLines = selectedData.split("\n");
                String oldName = oldLines[0].substring("Name: ".length());
                String oldEmail = oldLines[1].substring("Email: ".length());
                String oldRoom_type = oldLines[2].substring("Room Type: ".length());
                String oldCheck_in = oldLines[3].substring("Check in Date: ".length());
                String oldCheck_out = oldLines[4].substring("Check out Date: ".length());

                String[] newLines = editedData.split("\n");
                String newName = newLines[0].substring("Name: ".length());
                String newEmail = newLines[1].substring("Email: ".length());
                String newRoom_type = newLines[2].substring("Room Type: ".length());
                String newCheck_in = newLines[3].substring("Check in Date: ".length());
                String newCheck_out = newLines[4].substring("Check out Date: ".length());

                String updateQuery = String.format(
                        "UPDATE reservations SET name='%s', email='%s', room_type='%s', check_in='%s', check_out='%s' WHERE name='%s' AND email='%s'",
                        newName, newEmail, newRoom_type, newCheck_in, newCheck_out, oldName, oldEmail);
                int rowsAffected = statement.executeUpdate(updateQuery);

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Data updated in the database.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Error updating data in the database.");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error connecting to the database: " + ex.getMessage());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Class Not Found Error: " + e.getMessage());
        }
    }

    private void deleteDataFromDatabase(String selectedData) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost/pf";
            String username = "root";
            String password = "";

            try (Connection con = DriverManager.getConnection(url, username, password);
                    Statement statement = con.createStatement()) {

                String[] lines = selectedData.split("\n");

                String name = lines[0].substring("Name: ".length());
                String email = lines[1].substring("Email: ".length());

                String deleteQuery = String.format("DELETE FROM reservations WHERE name='%s' AND email='%s'", name,
                        email);
                int rowsAffected = statement.executeUpdate(deleteQuery);

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Data deleted from the database.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Error deleting data from the database.");
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error connecting to the database: " + ex.getMessage());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Class Not Found Error: " + e.getMessage());
        }
    }

    public void setData(String data) {
        displayTextArea.setText(data);
    }

}