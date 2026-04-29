package src.users;
import java.sql.*;

import src.FormConnection;
public class Faculty extends UserAccount{
    public Faculty(int facultyID, String name, String email, String password){
        super(facultyID, name, email, password);
    }

    public void viewAllHistory(String room){
        String sql = "SELECT users.name, " + room + ".sessionlogin, " + room + ".sessionlogout, " + room + ".deviceID, " + room + ".deviceType FROM " + room +
                " JOIN users ON " + room + ".userID = users.userID ORDER BY " + room + "ID DESC";
        try(PreparedStatement ps = FormConnection.connectDb().prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String name = rs.getString("name");
                Timestamp loginTime = rs.getTimestamp("sessionlogin");
                Timestamp logoutTime = rs.getTimestamp("sessionlogout");
                int deviceId = rs.getInt("deviceID");
                String deviceType = rs.getString("deviceType");
                System.out.println("User: " + name + ", Device: " + deviceType + " " + deviceId + ", Login: " + loginTime + ", Logout: " + logoutTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all use history");
        }
    }
    //notifications
    


    public int getRole(){
        return 2;
    }
}
