import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class BookingScreen extends JFrame {
    public BookingScreen() {
        setTitle("Book a Room");
        setSize(400, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(173, 216, 230)); // Light blue background

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 30, 80, 25);
        add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(120, 30, 200, 25);
        add(nameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(30, 70, 80, 25);
        add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(120, 70, 200, 25);
        add(phoneField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 110, 80, 25);
        add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(120, 110, 200, 25);
        add(emailField);

        JLabel checkInLabel = new JLabel("Check-in (YYYY-MM-DD):");
        checkInLabel.setBounds(30, 150, 200, 25);
        add(checkInLabel);

        JTextField checkInField = new JTextField();
        checkInField.setBounds(200, 150, 120, 25);
        add(checkInField);

        JLabel checkOutLabel = new JLabel("Check-out (YYYY-MM-DD):");
        checkOutLabel.setBounds(30, 190, 200, 25);
        add(checkOutLabel);

        JTextField checkOutField = new JTextField();
        checkOutField.setBounds(200, 190, 120, 25);
        add(checkOutField);

        JLabel roomLabel = new JLabel("Room No:");
        roomLabel.setBounds(30, 230, 80, 25);
        add(roomLabel);

        JComboBox<String> roomComboBox = new JComboBox<>();
        roomComboBox.setBounds(120, 230, 200, 25);
        add(roomComboBox);

        JLabel serviceLabel = new JLabel("Services:");
        serviceLabel.setBounds(30, 270, 80, 25);
        add(serviceLabel);

        JCheckBox breakfast = new JCheckBox("Breakfast (500)");
        breakfast.setBounds(120, 270, 150, 25);
        breakfast.setBackground(new Color(173, 216, 230));
        add(breakfast);

        JCheckBox wifi = new JCheckBox("WiFi (300)");
        wifi.setBounds(120, 300, 150, 25);
        wifi.setBackground(new Color(173, 216, 230));
        add(wifi);

        JCheckBox cleaning = new JCheckBox("Room Cleaning (700)");
        cleaning.setBounds(120, 330, 180, 25);
        cleaning.setBackground(new Color(173, 216, 230));
        add(cleaning);

        JButton bookButton = new JButton("Book Room");
        bookButton.setBounds(120, 370, 150, 30);
        bookButton.setBackground(new Color(0, 102, 204)); // Darker blue button
        bookButton.setForeground(Color.WHITE);
        add(bookButton);

        JLabel resultLabel = new JLabel("");
        resultLabel.setBounds(30, 410, 350, 30);
        add(resultLabel);

        HashMap<String, Integer> roomMap = new HashMap<>();

        try (Connection conn = DBConnector.getConnection()) {
            String sql = "SELECT room_id, room_number FROM rooms WHERE is_available = TRUE";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("room_id");
                String roomNo = rs.getString("room_number");
                roomMap.put(roomNo, id);
                roomComboBox.addItem(roomNo);
            }
        } catch (Exception e) {
            resultLabel.setText("Error loading rooms: " + e.getMessage());
        }

        bookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();
                String checkIn = checkInField.getText().trim();
                String checkOut = checkOutField.getText().trim();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty()) {
                    resultLabel.setText("Please fill all fields.");
                    return;
                }

                if (roomComboBox.getSelectedItem() == null) {
                    resultLabel.setText("No room selected.");
                    return;
                }

                String selectedRoomNo = roomComboBox.getSelectedItem().toString();
                int roomId = roomMap.get(selectedRoomNo);

                int serviceTotal = 0;
                if (breakfast.isSelected()) serviceTotal += 500;
                if (wifi.isSelected()) serviceTotal += 300;
                if (cleaning.isSelected()) serviceTotal += 700;

                try (Connection conn = DBConnector.getConnection()) {
                    String roomPriceQuery = "SELECT price FROM rooms WHERE room_id = ?";
                    PreparedStatement priceStmt = conn.prepareStatement(roomPriceQuery);
                    priceStmt.setInt(1, roomId);
                    ResultSet rs = priceStmt.executeQuery();

                    int roomPrice = 0;
                    if (rs.next()) roomPrice = rs.getInt("price");

                    int total = roomPrice + serviceTotal;

                    String insertBooking = "INSERT INTO bookings (customer_name, phone_number, email, room_id, check_in_date, check_out_date, total_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertBooking, Statement.RETURN_GENERATED_KEYS);
                    insertStmt.setString(1, name);
                    insertStmt.setString(2, phone);
                    insertStmt.setString(3, email);
                    insertStmt.setInt(4, roomId);
                    insertStmt.setString(5, checkIn);
                    insertStmt.setString(6, checkOut);
                    insertStmt.setInt(7, total);
                    insertStmt.executeUpdate();

                    ResultSet keys = insertStmt.getGeneratedKeys();
                    int bookingId = 0;
                    if (keys.next()) bookingId = keys.getInt(1);

                    if (breakfast.isSelected()) addService(conn, bookingId, "Breakfast");
                    if (wifi.isSelected()) addService(conn, bookingId, "WiFi");
                    if (cleaning.isSelected()) addService(conn, bookingId, "Room Cleaning");

                    String updateRoom = "UPDATE rooms SET is_available = FALSE WHERE room_id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateRoom);
                    updateStmt.setInt(1, roomId);
                    updateStmt.executeUpdate();

                    resultLabel.setText("Total: Rs. " + total + " — See you soon!");
                    roomComboBox.removeItem(selectedRoomNo);

                    JOptionPane.showMessageDialog(null, "Room " + selectedRoomNo + " booked successfully!");

                } catch (Exception ex) {
                    resultLabel.setText("Error: " + ex.getMessage());
                }
            }

            void addService(Connection conn, int bookingId, String serviceName) throws SQLException {
                String getServiceId = "SELECT service_id FROM services WHERE service_name = ?";
                PreparedStatement stmt = conn.prepareStatement(getServiceId);
                stmt.setString(1, serviceName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    int serviceId = rs.getInt("service_id");
                    String link = "INSERT INTO booking_services (booking_id, service_id) VALUES (?, ?)";
                    PreparedStatement linkStmt = conn.prepareStatement(link);
                    linkStmt.setInt(1, bookingId);
                    linkStmt.setInt(2, serviceId);
                    linkStmt.executeUpdate();
                }
            }
        });

        setVisible(true);
    }
}
