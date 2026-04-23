import java.sql.*;

public class ComputerStrategy implements StatusStrategy{
	public boolean statusStrategy(int id) {
		boolean status = false;
		try {
			Statement stmt = FormConnection.connect();

			String strSelect = "select status from pc where pcID = "+id;
			ResultSet rset = stmt.executeQuery(strSelect);
			rset.next();
			status = rset.getString("status").equals("available");

		} catch(Exception e) {
			System.out.println(e);
		} 
		return status;
	}
}
