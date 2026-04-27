package src.users;
import java.sql.*;

import src.FormConnection;
public class Faculty extends UserAccount{
    public Faculty(int facultyID, String name, String email, String password){
        super(facultyID, name, email, password);
    }

    public void viewUseHistory(int userID, String room){
        String sql = "SELECT * FROM " + room + " WHERE userID = ?";
        try(PreparedStatement ps = FormConnection.connect().getConnection().prepareStatement(sql)){
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Timestamp loginTime = rs.getTimestamp("sessionlogin");
                Timestamp logoutTime = rs.getTimestamp("sessionlogout");
                System.out.println("Login: " + loginTime + ", Logout: " + logoutTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving use history");
        }
    }
    public int getRole(){
        return 2;
    }
}
