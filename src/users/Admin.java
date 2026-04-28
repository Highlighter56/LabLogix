package src.users;
import java.sql.*;



import src.FormConnection;
public class Admin extends UserAccount{
    public Admin(int adminId, String name, String email, String password){
        super(adminId, name, email, password);
    }
    
    public UserAccount createUserAccount(String name, String email, String password, String role) throws SQLException{
        //only admin can create user accounts. Admin can also specify the role of the new user account.
        Connection conn = FormConnection.connect().getConnection();
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.executeUpdate();
            ResultSet keys= ps.getGeneratedKeys();
            if(!keys.next()){
                throw new RuntimeException("Failed to retrieve generated user ID");
            }
            //gets the auto incremented id from the database and makes a new user acount.
            int newID = keys.getInt(1);
            return switch (role) {
                case "student" -> new Student(newID, name, email, password);
                case "faculty" -> new Faculty(newID, name, email, password);
                case "admin" -> new Admin(newID, name, email, password);
                default -> throw new IllegalArgumentException("Invalid user role");
            };
        }
    }

    public void removeUser(int targetUserID){
        String sql = "DELETE FROM users WHERE userID = ?";
        try(PreparedStatement ps = FormConnection.connect().getConnection().prepareStatement(sql)){
            ps.setInt(1, targetUserID);
            ps.executeUpdate();
            System.out.println("User removed successfully");
        } catch (SQLException e) {
            e.printStackTrace();            throw new RuntimeException("Error removing user");
        } 
    }
    public void changeUserRole(int userID, String newRole){
        String sql = "UPDATE users SET role = ? WHERE userID = ?";
        try(PreparedStatement ps = FormConnection.connect().getConnection().prepareStatement(sql)){
            ps.setString(1, newRole);
            ps.setInt(2, userID);
            ps.executeUpdate();
            System.out.println("User role updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();            throw new RuntimeException("Error updating user role");
        } 
    }
    /* 
    private void manageUser(int targetUserID){
        String sql = "SELECT * FROM users WHERE userID = ?";
        try(PreparedStatement ps = FormConnection.connect().getConnection().prepareStatement(sql)){
            ps.setInt(1, targetUserID);
            //return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error managing user");
        }
    }*/
    public ResultSet viewAllUsers(){
        String sql = "SELECT userID, name, email, role FROM users";
        try(PreparedStatement ps = FormConnection.connect().getConnection().prepareStatement(sql)){
            return ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving users");
        }
    }
    public void viewAllHistory(String room){
        String sql = "SELECT users.name, " + room + ".sessionlogin, " + room + ".sessionlogout FROM " + room +
                " JOIN users ON " + room + ".userID = users.userID ORDER BY " + room + "ID DESC";
        try(PreparedStatement ps = FormConnection.connect().getConnection().prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
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
		String sql = "SELECT u.email, u.name, 'room259' AS room, r.sessionlogin " +
				"FROM room259 r JOIN users u ON u.userID = r.userID WHERE r.sessionlogout IS NULL " +
				"UNION ALL " +
				"SELECT u.email, u.name, 'room260' AS room, r.sessionlogin " +
				"FROM room260 r JOIN users u ON u.userID = r.userID WHERE r.sessionlogout IS NULL " +
				"ORDER BY room, sessionlogin";
		try (Connection conn = FormConnection.connectDb(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			System.out.println("\nUsers currently logged into devices:");
			boolean found = false;
			while (rs.next()) {
				found = true;
				System.out.println("- " + rs.getString("email") + " (" + rs.getString("name") + ") in " + rs.getString("room") + " since " + rs.getString("sessionlogin"));
			}
			if (!found) {
				System.out.println("No active sessions.");
			}
		} catch (Exception e) {
			System.out.println("Error reading active sessions: " + e.getMessage());
		}
	}
    public void logOutAllUsers() {
		try (Connection conn = FormConnection.connectDb(); Statement stmt = conn.createStatement()) {
			conn.setAutoCommit(false);
			int room259Rows = stmt.executeUpdate("UPDATE room259 SET sessionlogout = NOW(), currentOccupancy = 0 WHERE sessionlogout IS NULL");
			int room260Rows = stmt.executeUpdate("UPDATE room260 SET sessionlogout = NOW(), currentOccupancy = 0 WHERE sessionlogout IS NULL");
			stmt.executeUpdate("UPDATE pc SET status = 'available' WHERE status = 'in_use'");
			stmt.executeUpdate("UPDATE printer SET status = 'available' WHERE status = 'in_use'");
			stmt.executeUpdate("UPDATE printer3d SET status = 'available' WHERE status = 'in_use'");
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
