import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AvailableRoomsScreen extends JFrame {
    public AvailableRoomsScreen() {
        setTitle("Available Rooms");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        // Set background color of frame
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue

        // Table setup
        String[] columns = {"Room Number", "Type", "Price"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.setGridColor(Color.GRAY);
        table.setShowVerticalLines(false);

        // Scroll pane and border styling
        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(20, 20, 450, 300);
        pane.setBorder(new LineBorder(Color.DARK_GRAY, 2)); // Adds border around table

        add(pane);

        // Database logic
        try (Connection conn = DBConnector.getConnection()) {
            String query = "SELECT room_number, room_type, price FROM rooms WHERE is_available = TRUE";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String number = rs.getString("room_number");
                String type = rs.getString("room_type");
                String price = rs.getString("price");
                model.addRow(new Object[]{number, type, price});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage());
        }

        setVisible(true);
    }
}
