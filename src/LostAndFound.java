import javax.swing.*;
import java.sql.*;

public class LostAndFound {

    static final String DB_URL = "jdbc:mysql://localhost:3306/lost_and_found_db";
    static final String USER = "root"; 
    static final String PASS = "";     

    public static void main(String[] args) {
        String[] options = {"Report Lost Item", "Report Found Item", "View All Items", "Exit"};

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            while (true) {
                int choice = JOptionPane.showOptionDialog(
                        null,
                        "📌 Welcome!\n\nPlease choose an action below for the Lost and Found Reporting System.",
                        "Lost and Found System",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choice == 0 || choice == 1) {
                    String userName = JOptionPane.showInputDialog("👤 Please enter your full name (required to report an item):");
                    if (userName == null || userName.isBlank()) continue;

                    int userId = -1;

                    
                    String getUserSQL = "SELECT id FROM users WHERE name = ?";
                    try (PreparedStatement getUserStmt = conn.prepareStatement(getUserSQL)) {
                        getUserStmt.setString(1, userName);
                        ResultSet rs = getUserStmt.executeQuery();

                        if (rs.next()) {
                            userId = rs.getInt("id");
                        } else {
                            String insertUserSQL = "INSERT INTO users (name) VALUES (?)";
                            try (PreparedStatement insertUserStmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS)) {
                                insertUserStmt.setString(1, userName);
                                insertUserStmt.executeUpdate();
                                ResultSet keys = insertUserStmt.getGeneratedKeys();
                                if (keys.next()) {
                                    userId = keys.getInt(1);
                                }
                            }
                        }
                    }

                    String itemName = JOptionPane.showInputDialog("📦 Enter the name of the item you're reporting (e.g., Wallet, Phone):");
                    if (itemName == null || itemName.isBlank()) continue;

                    String description = JOptionPane.showInputDialog("📝 Provide a detailed description (color, brand, location, etc.):");
                    if (description == null || description.isBlank()) continue;

                    String status = (choice == 0) ? "Lost" : "Found";

                    String insertItemSQL = "INSERT INTO items (name, description, status, user_id) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(insertItemSQL)) {
                        stmt.setString(1, itemName);
                        stmt.setString(2, description);
                        stmt.setString(3, status);
                        stmt.setInt(4, userId);
                        stmt.executeUpdate();

                        String confirmation = String.format(
                                "✅ Item Reported Successfully!\n\n" +
                                        "Status   : %s\n" +
                                        "Item     : %s\n" +
                                        "Details  : %s\n" +
                                        "Reported by: %s",
                                status.toUpperCase(), itemName, description, userName
                        );
                        JOptionPane.showMessageDialog(null, confirmation);
                    }

                } else if (choice == 2) {
                    String querySQL = """
                            SELECT i.status, i.name, i.description, u.name AS user_name
                            FROM items i
                            JOIN users u ON i.user_id = u.id
                            """;

                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery(querySQL)) {

                        if (!rs.isBeforeFirst()) {
                            JOptionPane.showMessageDialog(null, "📭 No items have been reported yet.");
                        } else {
                            StringBuilder message = new StringBuilder("📋 List of Reported Items:\n\n");
                            int count = 1;
                            while (rs.next()) {
                                message.append(count++).append(". Status   : ").append(rs.getString("status")).append("\n")
                                        .append("   Item     : ").append(rs.getString("name")).append("\n")
                                        .append("   Details  : ").append(rs.getString("description")).append("\n")
                                        .append("   Reported by: ").append(rs.getString("user_name")).append("\n\n");
                            }
                            JOptionPane.showMessageDialog(null, message.toString());
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "👋 Thank you for using the Lost and Found System. Goodbye!");
                    break;
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "⚠ Error while accessing the database:\n" + e.getMessage());
        }
    }
}
