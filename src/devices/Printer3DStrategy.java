package src.devices;
import java.sql.*;

import src.FormConnection;

public class Printer3DStrategy implements StatusStrategy{
	public boolean statusStrategy(int id) {
		boolean status = false;
		try (Statement stmt = FormConnection.connect()) {
			String strSelect = "select status from printer3d where printer3dID = " + id;
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
