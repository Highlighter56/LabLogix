import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class PrinterStrategy implements StatusStrategy{
	public boolean statusStrategy(int id) {
		boolean status = false;
		try {
			BufferedReader br = new BufferedReader(new FileReader(".env"));
			String line = br.readLine();
			String db_url = line.substring(line.indexOf("=")+1);
			line = br.readLine();
			String db_user = line.substring(line.indexOf("=")+1);
			line = br.readLine();
			String db_password = line.substring(line.indexOf("=")+1);
			br.close();

			Connection conn = DriverManager.getConnection(db_url, db_user, db_password);
			Statement stmt = conn.createStatement();

			String strSelect = "select status from printer where printerID = "+id;
			ResultSet rset = stmt.executeQuery(strSelect);
			rset.next();
			status = rset.getString("status").equals("available");

		} catch(Exception e) {
			System.out.println(e);
		} 
		return status;
	}
}
