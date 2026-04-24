package src.users;
import java.sql.*;

import src.FormConnection;
public class Student extends UserAccount{
    public Student(int studnetID, String name, String email, String password){
        super(studnetID, name, email, password);
    }

    public void viewAvailability(){

    }
    public void viewUseHistory(){

    }
}
