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
        Connection conn = FormConnection.connectDb();
        String sql ="SELECT * FROM users WHERE email = ?";
        
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
                String role = rs.getString("userType");

                return switch (role) {
                    case "student" -> new Student(userID, name, email, storedPassword);
                    case "faculty" -> new Faculty(userID, name, email, storedPassword);
                    case "administrator" -> new Admin(userID, name, email, storedPassword);
                    default -> throw new IllegalArgumentException("Invalid user role");
                };
            }

            //create call to occuupancy tracker to record login time
    }

    public static UserAccount findByEmail(String email) throws SQLException {
        Connection conn = FormConnection.connectDb();
        String sql = "SELECT * FROM users WHERE email = ?";
        
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()){
                return null;
            }
            
            int userID = rs.getInt("userID");
            String name = rs.getString("name");
            String storedPassword = rs.getString("password");
            String role = rs.getString("userType");

            return switch (role) {
                case "student" -> new Student(userID, name, email, storedPassword);
                case "faculty" -> new Faculty(userID, name, email, storedPassword);
                case "administrator" -> new Admin(userID, name, email, storedPassword);
                default -> throw new IllegalArgumentException("Invalid user role");
            };
        }
    }  
    public void logout(){
        //create call to occupancy tracker to record logout time

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
