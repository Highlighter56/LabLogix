package src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class FormConnection {
	public static Connection connectDb() {
		String db_url;
		String db_user;
		String db_password;
		try (BufferedReader br = new BufferedReader(new FileReader(".env"))) {
			String line = br.readLine();
			db_url = line.substring(line.indexOf("=")+1);
			line = br.readLine();
			db_user = line.substring(line.indexOf("=")+1);
			line = br.readLine();
			db_password = line.substring(line.indexOf("=")+1);
			return DriverManager.getConnection(db_url, db_user, db_password);
		} catch(Exception e) {
			throw new RuntimeException("Database connection failed", e);
		}
	}

	public static Statement connect() {
		try {
			return connectDb().createStatement();
		} catch(Exception e) {
			throw new RuntimeException("Statement creation failed", e);
		}
	}
}
