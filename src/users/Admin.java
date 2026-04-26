package src.users;
import java.sql.*;



import src.FormConnection;
public class Admin extends UserAccount{
    public Admin(int adminId, String name, String email, String password){
        super(adminId, name, email, password);
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

    public int getRole(){
        return 3;
    }
    
}
