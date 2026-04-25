package src.devices;
import java.sql.*;

import src.FormConnection;

// import FormConnection;

public class PrinterStrategy implements StatusStrategy{
	public boolean statusStrategy(int id) {
		boolean status = false;
		try (Statement stmt = FormConnection.connect()) {
			String strSelect = "select status from printer where printerID = " + id;
			try (ResultSet rset = stmt.executeQuery(strSelect)) {
				if (rset.next()) {
					status = rset.getString("status").equals("available");
				}
			}

		} catch(Exception e) {
			System.out.println(e);
		} 
		return status;
	}
}
