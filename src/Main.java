package src;
import java.sql.*;

public class Main {

	public static void main(String[] args) {
		try {
			// Call this first to initalize db parameters
			Statement stmt = FormConnection.connect();


		} catch(Exception e) {
			System.out.println(e);
		} 

	}

}
