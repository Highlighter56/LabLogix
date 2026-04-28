package src.users;
import java.sql.*;

import src.FormConnection;
public class Faculty extends UserAccount{
    public Faculty(int facultyID, String name, String email, String password){
        super(facultyID, name, email, password);
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
    //notifications
    


    public int getRole(){
        return 2;
    }
}
