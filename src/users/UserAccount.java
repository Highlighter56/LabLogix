package src.users;
import java.sql.*;


import src.FormConnection;

public abstract class UserAccount {
    private int userID;
    private String name;
    private String email;
    protected String Password;

    public UserAccount(int userID, String name, String email, String Password){
        this.userID=userID;
        this.name=name;
        this.email=email;
        this.Password=Password;
    }

    public static UserAccount login(String email, String password)throws SQLException{
        //connects to the database creates a time stamp for the room. 
        Connection conn = FormConnection.connect().getConnection();
        String sql ="SELECT * FROM users WHERE email = ? AND password = ?";
        
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if(!rs.next()){
                    throw new IllegalArgumentException("Invalid email or password");
                }
                String storedPassword = rs.getString("password");

                if(!storedPassword.equals(password)){
                    throw new IllegalArgumentException("Invalid email or password");
                }

                int userID = rs.getInt("userID");
                String name = rs.getString("name");
                String role = rs.getString("role");

                return switch (role) {
                    case "student" -> new Student(userID, name, email, password);
                    case "faculty" -> new Faculty(userID, name, email, password);
                    case "admin" -> new Admin(userID, name, email, password);
                    default -> throw new IllegalArgumentException("Invalid user role");
                };
            }

            //create call to occuupancy tracker to record login time
    }  
    public void logout(){
        //create call to occupancy tracker to record logout time

    }
    public void viewUseHistory(int userID, String room){

        String sql = "SELECT sessionlogin, sessionlogout FROM " + room + " WHERE userID = ?";
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



    public int getUserID(){
        return userID;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }

    public abstract int getRole();
}
