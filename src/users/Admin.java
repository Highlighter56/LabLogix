package src.users;
import java.sql.*;

import src.FormConnection;
public class Admin extends UserAccount{
    public Admin(int adminId, String name, String email, String password){
        super(adminId, name, email, password);
    }

    private void manageUser(){

    }
    
}
