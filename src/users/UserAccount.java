package src.users;
import java.sql.*;
import java.text.Normalizer.Form;

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
    public static UserAccount createUserAccount(int userID, String name, String email, String password, String role) throws SQLException{
        Connection conn = FormConnection.connect().getConnection();
        String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?, ?)";
        try(PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setString(4, password);
            ps.setString(5, role);
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
