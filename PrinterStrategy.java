import java.sql.*;

public class PrinterStrategy implements StatusStrategy{
	public boolean statusStrategy(int id) {
		boolean status = false;
		try {
			Statement stmt = FormConnection.connect();

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
