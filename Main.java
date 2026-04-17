import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class Main {
	
	// ---Attributes---
	private static String db_url;
	private static String db_user;
	private static String db_password;

	public static void main(String[] args) {
		try {
			// Call this first to initalize db parameters
			setUpSql();

			

		} catch(Exception e) {
			System.out.println(e);
		} 

	}

	// sets db url, user, password
	public static void setUpSql() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(".env"));
			String line = br.readLine();
			db_url = line.substring(line.indexOf("=")+1);
			line = br.readLine();
			db_user = line.substring(line.indexOf("=")+1);
			line = br.readLine();
			db_password = line.substring(line.indexOf("=")+1);
			br.close();
		} catch(Exception e) {
			System.out.println(e);
		} 
	}

}
