package src.users;
import java.sql.*;

import src.FormConnection;
public class Student extends UserAccount{
    public Student(int studentID, String name, String email, String password){
        super(studentID, name, email, password);
    }
    public int getRole(){
        return 1;
    }
}
