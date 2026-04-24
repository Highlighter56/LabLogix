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
    public void login(){
        //connects to the database creates a time stamp for the room. 
    }
    public void logout(){

    }

    public int getRole(){
        return 0;
    }
}
