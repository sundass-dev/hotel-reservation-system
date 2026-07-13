import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomeScreen {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Hotel Reservation System");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Set background color (same light blue as AvailableRoomsScreen)
        frame.getContentPane().setBackground(new Color(240, 248, 255));

        // Styled title label
        JLabel titleLabel = new JLabel("Welcome to Royal Palace Hotel", SwingConstants.CENTER);
        titleLabel.setBounds(50, 20, 300, 30);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 51, 102));
        frame.add(titleLabel);

        // View Rooms Button
        JButton viewRoomsBtn = new JButton("View Available Rooms");
        viewRoomsBtn.setBounds(100, 80, 200, 40);
        viewRoomsBtn.setBackground(new Color(173, 216, 230)); // light blue
        viewRoomsBtn.setForeground(Color.BLACK);
        viewRoomsBtn.setFocusPainted(false);
        frame.add(viewRoomsBtn);

        // Book Room Button
        JButton bookRoomBtn = new JButton("Book a Room");
        bookRoomBtn.setBounds(100, 140, 200, 40);
        bookRoomBtn.setBackground(new Color(135, 206, 250)); // slightly different blue
        bookRoomBtn.setForeground(Color.BLACK);
        bookRoomBtn.setFocusPainted(false);
        frame.add(bookRoomBtn);

        // Button actions
        viewRoomsBtn.addActionListener(e -> new AvailableRoomsScreen());
        bookRoomBtn.addActionListener(e -> new BookingScreen());

        frame.setVisible(true);
    }
}
