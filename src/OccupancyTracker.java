package src;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import src.users.UserAccount;
import src.devices.Device;

public class OccupancyTracker { 
	
	// ---Attributes---
	private String timeStamp;
	public enum actions {LOGIN, LOGOUT}
	private actions action;
	private UserAccount person;
										
	// ---Constructors---
	public OccupancyTracker(actions action, UserAccount person) {
		this.timeStamp = currentDateTime();
		this.action = action;
		this.person = person;
	}

	// ---Getters---
	public String getTimeStamp() {
		return timeStamp;
	}
	public actions getAction() {
		return action;
	}
	public UserAccount getPerson() {
		return person;
	}

	// ---Setters---
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public void setAction(actions action) {
		this.action = action;
	}
	public void setPerson(UserAccount person) {
		this.person = person;
	}

	// ---Methods---
	// Get, and format to match databse, current date and time
	public String currentDateTime() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return now.format(formatter); // ex: 2026-04-23 16:06:33
	}

	// Record Login
	public void recordLogin(UserAccount user, Device device) {
		try (Statement stmt = FormConnection.connect()) {

			// Update Device Status
			String tableName = "";
			String idField = "";

			switch (device.getType()) {
				case COMPUTER:
					tableName = "pc";
					idField = "pcID";
					break;
				case PRINTER:
					tableName = "printer";
					idField = "printerID";
					break;
				case PRINTER3D:
					tableName = "printer3d";
					idField = "printer3dID";
					break;
			}

			String updateDeviceSQL = "UPDATE " + tableName +
					" SET status = 'in_use' WHERE " + idField + " = " + device.getId();
			stmt.executeUpdate(updateDeviceSQL);

			// Determine Room
			String roomTable = "";

			if (device.getType() == Device.types.COMPUTER) {
				String sql = "SELECT room259ID, room260ID FROM pc WHERE pcID = " + device.getId();
				try (ResultSet rs = stmt.executeQuery(sql)) {
					if (rs.next()) {
						if (rs.getObject("room259ID") != null) {
							roomTable = "room259";
						} else {
							roomTable = "room260";
						}
					}
				}

			} else if (device.getType() == Device.types.PRINTER) {
				String sql = "SELECT room259ID FROM printer WHERE printerID = " + device.getId();
				try (ResultSet rs = stmt.executeQuery(sql)) {
					if (rs.next()) {
						roomTable = "room259";
					}
				}

			} else if (device.getType() == Device.types.PRINTER3D) {
				String sql = "SELECT room260ID FROM printer3d WHERE printer3dID = " + device.getId();
				try (ResultSet rs = stmt.executeQuery(sql)) {
					if (rs.next()) {
						roomTable = "room260";
					}
				}
			}

			if (roomTable.isEmpty()) {
				throw new Exception("Device is not assigned to a room");
			}

			// Count active rows directly so occupancy is derived from current sessions,
			// then store that snapshot on the newly inserted login row.
			int currentOccupancy = countActiveSessions(stmt, roomTable);

			// Get userID
			int userID = -1;
			String userSQL = "SELECT userID FROM users WHERE email = ?";
			try (PreparedStatement userStmt = stmt.getConnection().prepareStatement(userSQL)) {
				userStmt.setString(1, user.getEmail());
				try (ResultSet rs = userStmt.executeQuery()) {
					if (rs.next()) {
						userID = rs.getInt("userID");
					} else {
						throw new Exception("User not found");
					}
				}
			}

			int newOccupancy = currentOccupancy + 1;

			// Insert New Session Row
			int capacity = roomTable.equals("room259") ? 25 : 30;

			String insertSQL = "INSERT INTO " + roomTable +
					" (sessionlogin, sessionlogout, userID, status, capacity, currentOccupancy) VALUES (" +
					"NOW(), NULL, " + userID + ", 'open', " + capacity + ", " + newOccupancy + ")";

			stmt.executeUpdate(insertSQL);

			// System.out.println("Recorded Login");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int countActiveSessions(Statement stmt, String roomTable) throws SQLException {
		String occSQL = "SELECT COUNT(*) AS activeCount FROM " + roomTable + " WHERE sessionlogout IS NULL";
		try (ResultSet rs = stmt.executeQuery(occSQL)) {
			if (rs.next()) {
				return rs.getInt("activeCount");
			}
		}
		return 0;
	}

	// Record Logout
	public void recordLogout(UserAccount user, Device device) {
		try (Statement stmt = FormConnection.connect()) {

			// Get userID
			int userID = -1;
			String userSQL = "SELECT userID FROM users WHERE email = ?";
			try (PreparedStatement userStmt = stmt.getConnection().prepareStatement(userSQL)) {
				userStmt.setString(1, user.getEmail());
				try (ResultSet rs = userStmt.executeQuery()) {
					if (rs.next()) {
						userID = rs.getInt("userID");
					} else {
						throw new Exception("User not found");
					}
				}
			}

			// Update Room Table
			String update260 = "UPDATE room260 " +
					"SET sessionlogout = NOW(), " +
					"currentOccupancy = GREATEST(currentOccupancy - 1, 0) " +
					"WHERE userID = " + userID + " AND sessionlogout IS NULL";

			int rowsAffected260 = stmt.executeUpdate(update260);

			int rowsAffected259 = 0;

			if (rowsAffected260 == 0) {
				String update259 = "UPDATE room259 " +
						"SET sessionlogout = NOW(), " +
						"currentOccupancy = GREATEST(currentOccupancy - 1, 0) " +
						"WHERE userID = " + userID + " AND sessionlogout IS NULL";

				rowsAffected259 = stmt.executeUpdate(update259);
			}

			// Update Device Status
			String tableName = "";
			String idField = "";

			switch (device.getType()) {
				case COMPUTER:
					tableName = "pc";
					idField = "pcID";
					break;
				case PRINTER:
					tableName = "printer";
					idField = "printerID";
					break;
				case PRINTER3D:
					tableName = "printer3d";
					idField = "printer3dID";
					break;
			}

			String updateDeviceSQL = "UPDATE " + tableName +
					" SET status = 'available' WHERE " + idField + " = " + device.getId();

			stmt.executeUpdate(updateDeviceSQL);

			if (rowsAffected260 > 0 || rowsAffected259 > 0) {
				System.out.println("Recorded Logout");
			} else {
				System.out.println("No session to log out of");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
