package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import src.users.Admin;
import src.users.Faculty;
import src.users.Student;
import src.users.UserAccount;
import src.devices.Device;

public class InteractiveTerminal {
	private static final int BACK = -1;
	private final Scanner scanner;

	public InteractiveTerminal() {
		this.scanner = new Scanner(System.in);
	}

	public void run() {
		System.out.println("=== LabLogix Terminal Simulator ===");
		while (true) {
			System.out.println("\nMain Actions:");
			System.out.println("1. Login");
			System.out.println("2. Logout");
			System.out.println("3. Exit");

			int choice = readInt("Choose an option: ");
			switch (choice) {
				case 1:
					handleLoginLoop();
					break;
				case 2:
					handleLogoutLoop();
					break;
				case 3:
					System.out.println("Goodbye.");
					scanner.close();
					return;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private void handleLoginLoop(){
		while (true) {
			System.out.println("\nLogin Flow (room -> device -> email -> password, type 'back' at room prompt to return):");
			int roomId = promptRoom();
			if (roomId == BACK) {
				return;
			}

			Device.types type = promptDeviceType(roomId);
			if (type == null) {
				continue;
			}

			List<Integer> availableIds = getAvailableDeviceIds(roomId, type);
			if (availableIds.isEmpty()) {
				System.out.println("No available devices found for that room/type.");
				if (!askRetryOrBack("Try a different room or device type? (y = retry, n = main menu): ")) {
					return;
				}
				continue;
			}

			Integer deviceId = promptDeviceId(availableIds, "Login");
			if (deviceId == null) {
				continue;
			}

			String email = readLine("Email: ");
			if ("back".equalsIgnoreCase(email)) {
				return;
			}
			String password = readLine("Password: ");

			UserAccount user;
			try {
				user = UserAccount.login(email, password);
			} catch (Exception e) {
				System.out.println("Login failed: " + e.getMessage());
				if (!askRetryOrBack("Retry login? (y = retry, n = main menu): ")) {
					return;
				}
				continue;
			}

			if (!recordLoginForDevice(user, roomId, deviceId, type)) {
				if (!askRetryOrBack("Retry login? (y = retry, n = main menu): ")) {
					return;
				}
				continue;
			}

			System.out.println("Welcome, " + user.getName());
			int role = user.getRole();
			if (role == 1) {
				studentMenu((Student) user);
				return;
			} else if (role == 2) {
				facultyMenu((Faculty) user);
				return;
			} else if (role == 3) {
				adminMenu((Admin) user);
				return;
			}

			System.out.println("Unknown role. Returning to main menu.");
			return;
		}
	}




	private void handleLogoutLoop() {
		while (true) {
			System.out.println("\nLogout Flow (type 'back' for email to return):");
			String email = readLine("Email: ");
			if ("back".equalsIgnoreCase(email)) {
				return;
			}

			UserAccount user;
			try {
				user = UserAccount.findByEmail(email);
			} catch (Exception e) {
				System.out.println("Error looking up user: " + e.getMessage());
				if (!askRetryOrBack("Retry logout? (y = retry, n = main menu): ")) {
					return;
				}
				continue;
			}
			if (user == null) {
				System.out.println("Unknown email.");
				if (!askRetryOrBack("Retry logout? (y = retry, n = main menu): ")) {
					return;
				}
				continue;
			}

			while (true) {
				System.out.println("Logout options:");
				System.out.println("1. Logout a specific device");
				System.out.println("2. Logout all devices");
				System.out.println("3. Back");
				int choice = readInt("Choose an option: ");
				switch (choice) {
					case 1:
						if (logoutSpecificDevice(user)) {
							return;
						}
						break;
					case 2:
						logoutAllDevices(user);
						return;
					case 3:
						return;
					default:
						System.out.println("Invalid option.");
				}
			}
		}
	}

	private void studentMenu(UserAccount user) {
		System.out.println("Student login complete. Returning to main menu.");
	}

	private void facultyMenu(Faculty user) {
		while (true) {
			System.out.println("\nFaculty Menu:");
			System.out.println("1. Login to another device");
			System.out.println("2. View user history");
			System.out.println("3. Back");
			int choice = readInt("Choose an option: ");
			switch (choice) {
				case 1:
					if (loginToDevice(user)) {
						return;
					}
					break;
				case 2:
					viewUserHistory(user);
					break;
				case 3:
					return;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private void adminMenu(Admin user) {
		while (true) {
			System.out.println("\nAdmin Menu:");
			System.out.println("1. Login to another device");
			System.out.println("2. View user history");
			System.out.println("3. Manage devices/labs");
			System.out.println("4. Manage users");
			System.out.println("5. Back");
			int choice = readInt("Choose an option: ");
			switch (choice) {
				case 1:
					if (loginToDevice(user)) {
						return;
					}
					break;
				case 2:
					viewUserHistory(user);
					break;
				case 3:
					manageDevicesPlaceholder(user);
					break;
				case 4:
					manageUsersMenu(user);
					break;
				case 5:
					return;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private boolean loginToDevice(UserAccount user) {
		while (true) {
			int roomId = promptRoom();
			if (roomId == BACK) {
				return false;
			}

			while (true) {
				Device.types type = promptDeviceType(roomId);
				if (type == null) {
					break;
				}

				List<Integer> availableIds = getAvailableDeviceIds(roomId, type);
				if (availableIds.isEmpty()) {
					System.out.println("No available devices found for that type in room " + roomId + ".");
					if (!askRetryOrBack("Try a different room or device type? (y = retry, n = back): ")) {
						return false;
					}
					continue;
				}

				Integer deviceId = promptDeviceId(availableIds, "Login");
				if (deviceId == null) {
					continue;
				}

				if (recordLoginForDevice(user, roomId, deviceId, type)) {
					System.out.println("Login attempt recorded. Returning to menu.");
					return true;
				}
				return false;
			}
		}
	}

	private void viewUserHistory(UserAccount user) {
		String roomTable = promptRoomTable();
		if (roomTable == null) {
			return;
		}

		if (user instanceof Faculty faculty) {
			faculty.viewAllHistory(roomTable);
			return;
		}

		if (user instanceof Admin admin) {
			admin.viewAllHistory(roomTable);
			return;
		}

		System.out.println("History lookup is only available for faculty and admin accounts.");
	}

	private void manageUsersMenu(Admin user) {
		while (true) {
			System.out.println("\nManage Users:");
			System.out.println("1. View all users");
			System.out.println("2. Create user");
			System.out.println("3. Change user role");
			System.out.println("4. Remove user");
			System.out.println("5. Back");
			int choice = readInt("Choose an option: ");
			switch (choice) {
				case 1:
					user.viewAllUsers();
					break;
				case 2:
					createUserViaAdmin(user);
					break;
				case 3:
					changeUserRoleViaAdmin(user);
					break;
				case 4:
					removeUserViaAdmin(user);
					break;
				case 5:
					return;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private void createUserViaAdmin(Admin user) {
		String name = readLine("Name: ");
		String email = readLine("Email: ");
		String password = readLine("Password: ");
		String role = readLine("Role (student, faculty, admin): ");
		try {
			UserAccount createdUser = user.createUserAccount(name, email, password, role);
			System.out.println("Created user: " + createdUser.getName() + " (" + createdUser.getEmail() + ")");
		} catch (Exception e) {
			System.out.println("Error creating user: " + e.getMessage());
		}
	}

	private void changeUserRoleViaAdmin(Admin user) {
		int userId = readInt("User ID: ");
		String newRole = readLine("New role (student, faculty, admin): ");
		try {
			user.changeUserRole(userId, newRole);
		} catch (Exception e) {
			System.out.println("Error updating role: " + e.getMessage());
		}
	}

	private void removeUserViaAdmin(Admin user) {
		int userId = readInt("User ID: ");
		try {
			user.removeUser(userId);
		} catch (Exception e) {
			System.out.println("Error removing user: " + e.getMessage());
		}
	}

	private int promptRoom() {
		while (true) {
			System.out.println("\nSelect Room:");
			System.out.println("1. Room 259");
			System.out.println("2. Room 260");
			System.out.println("3. Back");
			int roomChoice = readInt("Choose an option: ");
			if (roomChoice == 1) {
				return 259;
			}
			if (roomChoice == 2) {
				return 260;
			}
			if (roomChoice == 3) {
				return -1;
			}
			System.out.println("Invalid option.");
		}
	}

	private Device.types promptDeviceType(int roomId) {
		while (true) {
			System.out.println("\nSelect Device Type:");
			System.out.println("1. Computer");
			if (roomId == 259) {
				System.out.println("2. Printer");
			}
			if (roomId == 260) {
				System.out.println("2. 3D Printer");
			}
			System.out.println("3. Back");
			int typeChoice = readInt("Choose an option: ");

			if (typeChoice == 1) {
				return Device.types.COMPUTER;
			}
			if (typeChoice == 2 && roomId == 259) {
				return Device.types.PRINTER;
			}
			if (typeChoice == 2 && roomId == 260) {
				return Device.types.PRINTER3D;
			}
			if (typeChoice == 3) {
				return null;
			}
			System.out.println("Invalid option.");
		}
	}

	private Integer promptDeviceId(List<Integer> availableIds, String actionName) {
		while (true) {
			System.out.println("Available IDs: " + availableIds);
			String input = readLine(actionName + " Device ID (or type 'back'): ");
			if ("back".equalsIgnoreCase(input)) {
				return null;
			}
			try {
				int deviceId = Integer.parseInt(input);
				if (availableIds.contains(deviceId)) {
					return deviceId;
				}
				System.out.println("Invalid device ID. Enter one from the list or type back.");
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid device ID or type back.");
			}
		}
	}

	private List<Integer> getAvailableDeviceIds(int roomId, Device.types type) {
		List<Integer> ids = new ArrayList<>();
		String tableName;
		String idField;
		String roomField;

		switch (type) {
			case COMPUTER:
				tableName = "pc";
				idField = "pcID";
				roomField = roomId == 259 ? "room259ID" : "room260ID";
				break;
			case PRINTER:
				tableName = "printer";
				idField = "printerID";
				roomField = "room259ID";
				break;
			case PRINTER3D:
				tableName = "printer3d";
				idField = "printer3dID";
				roomField = "room260ID";
				break;
			default:
				return ids;
		}

		String sql = "SELECT " + idField + " FROM " + tableName + " WHERE status = 'available' AND " + roomField + " IS NOT NULL";
		try (Connection conn = FormConnection.connectDb(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				ids.add(rs.getInt(idField));
			}
		} catch (Exception e) {
			System.out.println("Error reading available devices: " + e.getMessage());
		}
		return ids;
	}

	private boolean recordLoginForDevice(UserAccount user, int roomId, int deviceId, Device.types type) {
		String roomTable = roomTableFor(roomId);
		String deviceTable = deviceTableFor(type);
		String deviceIdField = deviceIdFieldFor(type);
		if (roomTable == null || deviceTable == null || deviceIdField == null) {
			System.out.println("Unable to resolve room or device table for login.");
			return false;
		}

		String roomOccupancySql = "SELECT COUNT(*) AS activeCount FROM " + roomTable + " WHERE sessionlogout IS NULL";
		String updateDeviceSql = "UPDATE " + deviceTable + " SET status = 'in_use' WHERE " + deviceIdField + " = ?";
		String insertSql = "INSERT INTO " + roomTable + " (sessionlogin, sessionlogout, userID, deviceID, deviceType, status, capacity, currentOccupancy) VALUES (NOW(), NULL, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = FormConnection.connectDb()) {
			conn.setAutoCommit(false);
			int activeCount = 0;
			try (PreparedStatement occStmt = conn.prepareStatement(roomOccupancySql); ResultSet rs = occStmt.executeQuery()) {
				if (rs.next()) {
					activeCount = rs.getInt("activeCount");
				}
			}

			int capacity = roomId == 259 ? 25 : 30;
			int newOccupancy = activeCount + 1;
			String roomStatus = newOccupancy >= capacity ? "full" : "open";

			try (PreparedStatement updateDeviceStmt = conn.prepareStatement(updateDeviceSql)) {
				updateDeviceStmt.setInt(1, deviceId);
				if (updateDeviceStmt.executeUpdate() == 0) {
					throw new SQLException("Device not found");
				}
			}

			try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
				insertStmt.setInt(1, user.getUserID());
				insertStmt.setInt(2, deviceId);
				insertStmt.setString(3, type.name().toLowerCase());
				insertStmt.setString(4, roomStatus);
				insertStmt.setInt(5, capacity);
				insertStmt.setInt(6, newOccupancy);
				insertStmt.executeUpdate();
			}

			conn.commit();
			return true;
		} catch (Exception e) {
			System.out.println("Login recording failed: " + e.getMessage());
			return false;
		}
	}

	private boolean logoutSpecificDevice(UserAccount user) {
		int roomId = promptRoom();
		if (roomId == BACK) {
			return false;
		}

		Device.types type = promptDeviceType(roomId);
		if (type == null) {
			return false;
		}

		List<Integer> activeIds = getActiveDeviceIds(roomId, user.getUserID(), type);
		if (activeIds.isEmpty()) {
			System.out.println("No active sessions found for that room/type.");
			return false;
		}

		Integer deviceId = promptDeviceId(activeIds, "Logout");
		if (deviceId == null) {
			return false;
		}

		String roomTable = roomTableFor(roomId);
		String deviceTable = deviceTableFor(type);
		String deviceIdField = deviceIdFieldFor(type);
		String updateRoomSql = "UPDATE " + roomTable + " SET sessionlogout = NOW(), currentOccupancy = GREATEST(currentOccupancy - 1, 0) WHERE userID = ? AND deviceID = ? AND sessionlogout IS NULL";
		String updateDeviceSql = "UPDATE " + deviceTable + " SET status = 'available' WHERE " + deviceIdField + " = ?";

		try (Connection conn = FormConnection.connectDb()) {
			conn.setAutoCommit(false);
			int rows;
			try (PreparedStatement roomStmt = conn.prepareStatement(updateRoomSql)) {
				roomStmt.setInt(1, user.getUserID());
				roomStmt.setInt(2, deviceId);
				rows = roomStmt.executeUpdate();
			}
			if (rows == 0) {
				System.out.println("No active session found for that device.");
				conn.rollback();
				return false;
			}

			try (PreparedStatement deviceStmt = conn.prepareStatement(updateDeviceSql)) {
				deviceStmt.setInt(1, deviceId);
				deviceStmt.executeUpdate();
			}

			conn.commit();
			System.out.println("Logged out device " + deviceId + " from " + roomTable + ".");
			return true;
		} catch (Exception e) {
			System.out.println("Logout failed: " + e.getMessage());
			return false;
		}
	}

	private void logoutAllDevices(UserAccount user) {
		int totalRows = 0;
		try (Connection conn = FormConnection.connectDb()) {
			conn.setAutoCommit(false);
			totalRows += logoutAllDevicesInRoom(conn, user.getUserID(), 259);
			totalRows += logoutAllDevicesInRoom(conn, user.getUserID(), 260);
			conn.commit();
			if (totalRows == 0) {
				System.out.println("No active sessions found for that user.");
			} else {
				System.out.println("Logged out all active devices for " + user.getEmail() + ".");
			}
		} catch (Exception e) {
			System.out.println("Logout all failed: " + e.getMessage());
		}
	}

	private int logoutAllDevicesInRoom(Connection conn, int userId, int roomId) throws Exception {
		String roomTable = roomTableFor(roomId);
		String selectSql = "SELECT deviceID, deviceType FROM " + roomTable + " WHERE userID = ? AND sessionlogout IS NULL";
		int updated = 0;
		try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
			selectStmt.setInt(1, userId);
			try (ResultSet rs = selectStmt.executeQuery()) {
				while (rs.next()) {
					int deviceId = rs.getInt("deviceID");
					String deviceType = rs.getString("deviceType");
					String deviceTable = deviceTableFor(deviceType);
					String deviceIdField = deviceIdFieldFor(deviceType);
					String updateRoomSql = "UPDATE " + roomTable + " SET sessionlogout = NOW(), currentOccupancy = GREATEST(currentOccupancy - 1, 0) WHERE userID = ? AND deviceID = ? AND sessionlogout IS NULL";
					String updateDeviceSql = "UPDATE " + deviceTable + " SET status = 'available' WHERE " + deviceIdField + " = ?";
					try (PreparedStatement roomStmt = conn.prepareStatement(updateRoomSql); PreparedStatement deviceStmt = conn.prepareStatement(updateDeviceSql)) {
						roomStmt.setInt(1, userId);
						roomStmt.setInt(2, deviceId);
						updated += roomStmt.executeUpdate();
						deviceStmt.setInt(1, deviceId);
						deviceStmt.executeUpdate();
					}
				}
			}
		}
		return updated;
	}

	private List<Integer> getActiveDeviceIds(int roomId, int userId, Device.types type) {
		List<Integer> ids = new ArrayList<>();
		String roomTable = roomTableFor(roomId);
		String sql = "SELECT deviceID FROM " + roomTable + " WHERE userID = ? AND deviceType = ? AND sessionlogout IS NULL";
		try (Connection conn = FormConnection.connectDb(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			stmt.setString(2, type.name().toLowerCase());
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					ids.add(rs.getInt("deviceID"));
				}
			}
		} catch (Exception e) {
			System.out.println("Error reading active devices: " + e.getMessage());
		}
		return ids;
	}

	private String roomTableFor(int roomId) {
		if (roomId == 259) {
			return "room259";
		}
		if (roomId == 260) {
			return "room260";
		}
		return null;
	}

	private String promptRoomTable() {
		while (true) {
			System.out.println("\nSelect Room Table:");
			System.out.println("1. room259");
			System.out.println("2. room260");
			System.out.println("3. Back");
			int choice = readInt("Choose an option: ");
			if (choice == 1) {
				return "room259";
			}
			if (choice == 2) {
				return "room260";
			}
			if (choice == 3) {
				return null;
			}
			System.out.println("Invalid option.");
		}
	}

	private String deviceTableFor(Object typeValue) {
		String type = typeValue.toString().toLowerCase();
		return switch (type) {
			case "computer" -> "pc";
			case "printer" -> "printer";
			case "printer3d" -> "printer3d";
			default -> null;
		};
	}

	private String deviceIdFieldFor(Object typeValue) {
		String type = typeValue.toString().toLowerCase();
		return switch (type) {
			case "computer" -> "pcID";
			case "printer" -> "printerID";
			case "printer3d" -> "printer3dID";
			default -> null;
		};
	}

	private void manageDevicesPlaceholder(Admin userAdmin) {
		while (true) {
			System.out.println("\nManage Devices/Labs:");
			System.out.println("1. View users currently logged into devices");
			System.out.println("2. Set device status to available");
			System.out.println("3. Set device status to offline");
			System.out.println("4. Log out all users");
			System.out.println("5. Back");
			int choice = readInt("Choose an option: ");
			switch (choice) {
				case 1:
					userAdmin.showLoggedInUsers();
					break;
				case 2:
					setDeviceStatusForAdmin("available");
					break;
				case 3:
					setDeviceStatusForAdmin("offline");
					break;
				case 4:
					userAdmin.logOutAllUsers();
					break;
				case 5:
					return;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private void setDeviceStatusForAdmin(String targetStatus) {
		while (true) {
			int roomId = promptRoom();
			if (roomId == BACK) {
				return;
			}

			Device.types type = promptDeviceType(roomId);
			if (type == null) {
				continue;
			}

			Map<Integer, String> statuses = getDeviceStatusMap(roomId, type);
			if (statuses.isEmpty()) {
				System.out.println("No devices found for that room/type.");
				continue;
			}

			System.out.println("Devices for this room/type:");
			for (Map.Entry<Integer, String> entry : statuses.entrySet()) {
				String lockNote = "in_use".equalsIgnoreCase(entry.getValue()) ? " [locked - user logged in]" : "";
				System.out.println("- " + entry.getKey() + " status=" + entry.getValue() + lockNote);
			}

			String input = readLine("Device ID to set " + targetStatus + " (or type 'back'): ");
			if ("back".equalsIgnoreCase(input)) {
				continue;
			}

			int deviceId;
			try {
				deviceId = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid device ID or type back.");
				continue;
			}

			if (!statuses.containsKey(deviceId)) {
				System.out.println("That device ID is not valid for the selected room/type.");
				continue;
			}

			String currentStatus = statuses.get(deviceId);
			if ("in_use".equalsIgnoreCase(currentStatus)) {
				System.out.println("Cannot change this device. It is currently in use by a user.");
				continue;
			}

			if (targetStatus.equalsIgnoreCase(currentStatus)) {
				System.out.println("Device is already " + targetStatus + ".");
				continue;
			}

			if (updateDeviceStatus(deviceId, type, targetStatus)) {
				System.out.println("Device " + deviceId + " set to " + targetStatus + ".");
			}
			return;
		}
	}

	private Map<Integer, String> getDeviceStatusMap(int roomId, Device.types type) {
		Map<Integer, String> result = new LinkedHashMap<>();
		String tableName;
		String idField;
		String roomField;

		switch (type) {
			case COMPUTER:
				tableName = "pc";
				idField = "pcID";
				roomField = roomId == 259 ? "room259ID" : "room260ID";
				break;
			case PRINTER:
				tableName = "printer";
				idField = "printerID";
				roomField = "room259ID";
				break;
			case PRINTER3D:
				tableName = "printer3d";
				idField = "printer3dID";
				roomField = "room260ID";
				break;
			default:
				return result;
		}

		String sql = "SELECT " + idField + ", status FROM " + tableName + " WHERE " + roomField + " IS NOT NULL ORDER BY " + idField;
		try (Connection conn = FormConnection.connectDb(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				result.put(rs.getInt(idField), rs.getString("status"));
			}
		} catch (Exception e) {
			System.out.println("Error reading device statuses: " + e.getMessage());
		}
		return result;
	}

	private boolean updateDeviceStatus(int deviceId, Device.types type, String newStatus) {
		String tableName;
		String idField;
		switch (type) {
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
			default:
				return false;
		}

		String sql = "UPDATE " + tableName + " SET status = ? WHERE " + idField + " = ?";
		try (Connection conn = FormConnection.connectDb(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, newStatus);
			stmt.setInt(2, deviceId);
			return stmt.executeUpdate() > 0;
		} catch (Exception e) {
			System.out.println("Error updating device status: " + e.getMessage());
			return false;
		}
	}

	// private void logOutAllUsers() {
	// 	try (Connection conn = FormConnection.connectDb(); Statement stmt = conn.createStatement()) {
	// 		conn.setAutoCommit(false);
	// 		int room259Rows = stmt.executeUpdate("UPDATE room259 SET sessionlogout = NOW(), currentOccupancy = 0 WHERE sessionlogout IS NULL");
	// 		int room260Rows = stmt.executeUpdate("UPDATE room260 SET sessionlogout = NOW(), currentOccupancy = 0 WHERE sessionlogout IS NULL");
	// 		stmt.executeUpdate("UPDATE pc SET status = 'available' WHERE status = 'in_use'");
	// 		stmt.executeUpdate("UPDATE printer SET status = 'available' WHERE status = 'in_use'");
	// 		stmt.executeUpdate("UPDATE printer3d SET status = 'available' WHERE status = 'in_use'");
	// 		conn.commit();
	// 		System.out.println("Logged out all users. room259 rows updated: " + room259Rows + ", room260 rows updated: " + room260Rows);
	// 	} catch (Exception e) {
	// 		System.out.println("Error logging out all users: " + e.getMessage());
	// 	}
	// }

	private int readInt(String prompt) {
		while (true) {
			String input = readLine(prompt);
			try {
				return Integer.parseInt(input.trim());
			} catch (NumberFormatException e) {
				System.out.println("Please enter a valid number.");
			}
		}
	}

	private String readLine(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine().trim();
	}

	private boolean askRetryOrBack(String prompt) {
		while (true) {
			String choice = readLine(prompt).toLowerCase();
			if ("y".equals(choice)) {
				return true;
			}
			if ("n".equals(choice)) {
				return false;
			}
			System.out.println("Enter 'y' or 'n'.");
		}
	}
}
