package src.users;
import java.sql.*;

import src.FormConnection;
public class Student extends UserAccount{
    public Student(int studentID, String name, String email, String password){
        super(studentID, name, email, password);
    }
    //perhaps not needed
    public void viewAvailability(String room){
        //idk if thats even the correct statement. Does grabbing the session login and logout times from the room tables work?
        String sql = "SELECT sessionlogin, sessionlogout FROM " + room + " ORDER BY " + room + "ID DESC LIMIT 1";
        try(PreparedStatement ps = FormConnection.connect().getConnection().prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp loginTime = rs.getTimestamp("sessionlogin");
                Timestamp logoutTime = rs.getTimestamp("sessionlogout");
                System.out.println("Last Session - Login: " + loginTime + ", Logout: " + logoutTime);
            } else {
                System.out.println("No sessions found for " + room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving availability");
        }
    }


    public int getRole(){
        return 1;
    }
}
