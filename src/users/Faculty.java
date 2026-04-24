package src.users;
import java.sql.*;

import src.FormConnection;
public class Faculty extends UserAccount{
    public Faculty(int facultyID, String name, String email, String password){
        super(facultyID, name, email, password);
    }

    public void viewUseHistory(){
        
    }
}
