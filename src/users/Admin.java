package src.users;
import java.sql.*;



import src.FormConnection;
public class Admin extends UserAccount{
    public Admin(int adminId, String name, String email, String password){
        super(adminId, name, email, password);
    }
    
    public UserAccount createUserAccount(String name, String email, String password, String role) throws SQLException{
        //only admin can create user accounts. Admin can also specify the role of the new user account.
        String normalizedRole = role.toLowerCase();
        Connection conn = FormConnection.connectDb();
        String sql = "INSERT INTO users (name, email, password, userType) VALUES (?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, normalizedRole.equals("admin") ? "administrator" : normalizedRole);
            ps.executeUpdate();
            ResultSet keys= ps.getGeneratedKeys();
            if(!keys.next()){
                throw new RuntimeException("Failed to retrieve generated user ID");
            }
            //gets the auto incremented id from the database and makes a new user acount.
            int newID = keys.getInt(1);
            return switch (normalizedRole) {
                case "student" -> new Student(newID, name, email, password);
                case "faculty" -> new Faculty(newID, name, email, password);
                case "admin", "administrator" -> new Admin(newID, name, email, password);
                default -> throw new IllegalArgumentException("Invalid user role");
            };
        }
    }

    public void removeUser(int targetUserID){
        try (Connection conn = FormConnection.connectDb()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement("UPDATE pc SET userID = NULL WHERE userID = ?")) {
                ps1.setInt(1, targetUserID);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement("UPDATE printer SET userID = NULL WHERE userID = ?")) {
                ps2.setInt(1, targetUserID);
                ps2.executeUpdate();
            }
            try (PreparedStatement ps3 = conn.prepareStatement("UPDATE printer3d SET userID = NULL WHERE userID = ?")) {
                ps3.setInt(1, targetUserID);
                ps3.executeUpdate();
            }
            try (PreparedStatement ps4 = conn.prepareStatement("UPDATE room259 SET sessionlogout = NOW(), currentOccupancy = GREATEST(currentOccupancy - 1, 0) WHERE userID = ? AND sessionlogout IS NULL")) {
                ps4.setInt(1, targetUserID);
                ps4.executeUpdate();
            }
            try (PreparedStatement ps5 = conn.prepareStatement("UPDATE room260 SET sessionlogout = NOW(), currentOccupancy = GREATEST(currentOccupancy - 1, 0) WHERE userID = ? AND sessionlogout IS NULL")) {
                ps5.setInt(1, targetUserID);
                ps5.executeUpdate();
            }
            try (PreparedStatement del = conn.prepareStatement("DELETE FROM users WHERE userID = ?")) {
                del.setInt(1, targetUserID);
                int rows = del.executeUpdate();
                if (rows == 0) {
                    conn.rollback();
                    System.out.println("No user found with ID " + targetUserID);
                    return;
                }
            }
            conn.commit();
            System.out.println("User removed successfully");
        } catch (SQLException e) {
            System.out.println("Error removing user: " + e.getMessage());
        }
    }
    public void changeUserRole(int userID, String newRole){
        String normalized = newRole.toLowerCase();
        normalized = normalized.equals("admin") ? "administrator" : normalized;
        String sql = "UPDATE users SET userType = ? WHERE userID = ?";
        try (Connection conn = FormConnection.connectDb(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, normalized);
            ps.setInt(2, userID);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                System.out.println("No user found with ID " + userID);
            } else {
                System.out.println("User role updated successfully");
            }
        } catch (SQLException e) {
            System.out.println("SQL error updating user role: " + e.getMessage());
        }
    }
    public void viewAllUsers(){
        String sql = "SELECT userID, name, email, userType FROM users";
        try(PreparedStatement ps = FormConnection.connectDb().prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("User: " + rs.getInt("userID") + " | " + rs.getString("name") + " | " + rs.getString("email") + " | " + rs.getString("userType"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving users");
        }
    }
    public void viewAllHistory(String room){
        String sql = "SELECT users.name, " + room + ".sessionlogin, " + room + ".sessionlogout FROM " + room +
                " JOIN users ON " + room + ".userID = users.userID ORDER BY " + room + "ID DESC";
        try (PreparedStatement ps = FormConnection.connectDb().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                Timestamp loginTime = rs.getTimestamp("sessionlogin");
                Timestamp logoutTime = rs.getTimestamp("sessionlogout");
                System.out.println("User: " + name + ", Login: " + loginTime + ", Logout: " + logoutTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all use history");
        }
    }
    //manage devices or labs : view current users who are logged into devices. 
    //can set status to available and offline 
    //can force logout all users
    public void showLoggedInUsers() {
        System.out.println("\nUsers currently logged into devices:");
        boolean found = false;
        // PC entries
        String pcSql = "SELECT u.email, u.name, pc.pcID AS deviceID, pc.room260ID, pc.room259ID, pc.status, u.userID FROM pc JOIN users u ON pc.userID = u.userID WHERE pc.userID IS NOT NULL";
        try (Connection conn = FormConnection.connectDb(); PreparedStatement ps = conn.prepareStatement(pcSql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                found = true;
                String room = rs.getInt("room259ID") > 0 ? "room259" : "room260";
                System.out.println("- " + rs.getString("email") + " (" + rs.getString("name") + ") in " + room + " on computer " + rs.getInt("deviceID"));
            }
        } catch (Exception e) {
            System.out.println("Error reading PC sessions: " + e.getMessage());
        }
        // Printer entries
        String printerSql = "SELECT u.email, u.name, p.printerID AS deviceID, p.room259ID, p.status FROM printer p JOIN users u ON p.userID = u.userID WHERE p.userID IS NOT NULL";
        try (Connection conn = FormConnection.connectDb(); PreparedStatement ps = conn.prepareStatement(printerSql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                found = true;
                System.out.println("- " + rs.getString("email") + " (" + rs.getString("name") + ") in room259 on printer " + rs.getInt("deviceID"));
            }
        } catch (Exception e) {
            System.out.println("Error reading printer sessions: " + e.getMessage());
        }
        // Printer3D entries
        String p3dSql = "SELECT u.email, u.name, p3.printer3dID AS deviceID, p3.room260ID, p3.status FROM printer3d p3 JOIN users u ON p3.userID = u.userID WHERE p3.userID IS NOT NULL";
        try (Connection conn = FormConnection.connectDb(); PreparedStatement ps = conn.prepareStatement(p3dSql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                found = true;
                System.out.println("- " + rs.getString("email") + " (" + rs.getString("name") + ") in room260 on printer3d " + rs.getInt("deviceID"));
            }
        } catch (Exception e) {
            System.out.println("Error reading printer3d sessions: " + e.getMessage());
        }
        if (!found) {
            System.out.println("No active sessions.");
        }
	}
    public void logOutAllUsers() {
		try (Connection conn = FormConnection.connectDb(); Statement stmt = conn.createStatement()) {
			conn.setAutoCommit(false);
			int room259Rows = stmt.executeUpdate("UPDATE room259 SET sessionlogout = NOW(), currentOccupancy = 0 WHERE sessionlogout IS NULL");
			int room260Rows = stmt.executeUpdate("UPDATE room260 SET sessionlogout = NOW(), currentOccupancy = 0 WHERE sessionlogout IS NULL");
            stmt.executeUpdate("UPDATE pc SET status = 'available', userID = NULL WHERE status = 'in_use'");
            stmt.executeUpdate("UPDATE printer SET status = 'available', userID = NULL WHERE status = 'in_use'");
            stmt.executeUpdate("UPDATE printer3d SET status = 'available', userID = NULL WHERE status = 'in_use'");
			conn.commit();
			System.out.println("Logged out all users. room259 rows updated: " + room259Rows + ", room260 rows updated: " + room260Rows);
		} catch (Exception e) {
			System.out.println("Error logging out all users: " + e.getMessage());
		}
	}
    

    public int getRole(){
        return 3;
    }
    
}
