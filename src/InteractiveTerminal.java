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

import Test.Authenticate;
import Test.UserAccount;
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

	private void handleLoginLoop() {
		while (true) {
			System.out.println("\nLogin Flow (type 'back' for email to return):");
			String email = readLine("Email: ");
			if ("back".equalsIgnoreCase(email)) {
				return;
			}
			String password = readLine("Password: ");

			Authenticate.AuthResult result = Authenticate.authenticate(email, password);
			if (!result.isAuthenticated()) {
				System.out.println("Login failed: " + result.getMessage());
				if (!askRetryOrBack("Retry login? (y = retry, n = main menu): ")) {
					return;
				}
				continue;
			}

			UserAccount user = result.getUser();
			System.out.println("Welcome, " + user.getEmail() + " (" + user.getRole() + ")");
			showNotificationsPlaceholder(user);

			String role = user.getRole().toLowerCase();
			if ("student".equals(role)) {
				studentMenu(user);
			} else if ("faculty".equals(role)) {
				facultyMenu(user);
			} else if ("administrator".equals(role)) {
				adminMenu(user);
			} else {
				System.out.println("Unknown role. Returning to main menu.");
			}
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

			UserAccount user = Authenticate.findUserByEmail(email);
			if (user == null) {
				System.out.println("Unknown email.");
				if (!askRetryOrBack("Retry logout? (y = retry, n = main menu): ")) {
					return;
				}
				continue;
			}

			if (!hasActiveSession(user.getUserID())) {
				System.out.println("This user has no active session.");
				if (!askRetryOrBack("Retry logout? (y = retry, n = main menu): ")) {
					return;
				}
				continue;
			}

			while (true) {
				int roomId = promptRoom();
				if (roomId == BACK) {
					break;
				}

				while (true) {
					Device.types type = promptDeviceType(roomId);
					if (type == null) {
						break;
					}

					List<Integer> availableIds = getAvailableDeviceIds(roomId, type);
					if (availableIds.isEmpty()) {
						System.out.println("No matching devices are currently in use in that room/type.");
						if (!askRetryOrBack("Retry logout? (y = retry, n = back): ")) {
							return;
						}
						continue;
					}

					Integer deviceId = promptDeviceId(availableIds, "Logout");
					if (deviceId == null) {
						continue;
					}

					// !!! Missing session->device linkage in schema means we cannot verify
					// !!! that this exact device belongs to this exact active user session.
					Device device = new Device(deviceId, type);
					OccupancyTracker tracker = new OccupancyTracker(OccupancyTracker.actions.LOGOUT, user);
					tracker.recordLogout(user, device);
					System.out.println("Logout attempt recorded. Returning to main menu.");
					return;
				}
			}
		}
	}

	private void studentMenu(UserAccount user) {
		while (true) {
			System.out.println("\nStudent Menu:");
			System.out.println("1. Login to a device");
			System.out.println("2. Back");
			int choice = readInt("Choose an option: ");
			switch (choice) {
				case 1:
					if (loginToDevice(user)) {
						return;
					}
					break;
				case 2:
					return;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private void facultyMenu(UserAccount user) {
		while (true) {
			System.out.println("\nFaculty Menu:");
			System.out.println("1. Login to a device");
			System.out.println("2. View user history");
			System.out.println("3. Send notification");
			System.out.println("4. Back");
			int choice = readInt("Choose an option: ");
			switch (choice) {
				case 1:
					if (loginToDevice(user)) {
						return;
					}
					break;
				case 2:
					viewUserHistoryPlaceholder();
					break;
				case 3:
					sendNotificationPlaceholder();
					break;
				case 4:
					return;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private void adminMenu(UserAccount user) {
		while (true) {
			System.out.println("\nAdmin Menu:");
			System.out.println("1. Login to a device");
			System.out.println("2. View user history");
			System.out.println("3. Send notification");
			System.out.println("4. Manage devices/labs");
			System.out.println("5. Back");
			int choice = readInt("Choose an option: ");
			switch (choice) {
				case 1:
					if (loginToDevice(user)) {
						return;
					}
					break;
				case 2:
					viewUserHistoryPlaceholder();
					break;
				case 3:
					sendNotificationPlaceholder();
					break;
				case 4:
					manageDevicesPlaceholder();
					break;
				case 5:
					return;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private boolean loginToDevice(UserAccount user) {
		if (hasActiveSession(user.getUserID())) {
			System.out.println("One-device rule: user already has an active session.");
			return false;
		}

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

				Device device = new Device(deviceId, type);
				OccupancyTracker tracker = new OccupancyTracker(OccupancyTracker.actions.LOGIN, user);
				tracker.recordLogin(user, device);
				System.out.println("Login attempt recorded. Returning to main menu.");
				return true;
			}
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

	private boolean hasActiveSession(int userId) {
		return hasActiveSessionInRoom("room259", userId) || hasActiveSessionInRoom("room260", userId);
	}

	private boolean hasActiveSessionInRoom(String roomTable, int userId) {
		String sql = "SELECT 1 FROM " + roomTable + " WHERE userID = ? AND sessionlogout IS NULL LIMIT 1";
		try (Connection conn = FormConnection.connectDb(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, userId);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		} catch (Exception e) {
			System.out.println("Error checking active session in " + roomTable + ": " + e.getMessage());
			return false;
		}
	}

	private boolean deviceExists(int deviceId, Device.types type) {
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

		String sql = "SELECT 1 FROM " + tableName + " WHERE " + idField + " = ? LIMIT 1";
		try (Connection conn = FormConnection.connectDb(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, deviceId);
			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		} catch (Exception e) {
			System.out.println("Error validating device: " + e.getMessage());
			return false;
		}
	}

	private boolean isDeviceAvailable(int deviceId, Device.types type) {
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

		String sql = "SELECT status FROM " + tableName + " WHERE " + idField + " = ?";
		try (Connection conn = FormConnection.connectDb(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, deviceId);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return "available".equalsIgnoreCase(rs.getString("status"));
				}
			}
		} catch (Exception e) {
			System.out.println("Error reading device status: " + e.getMessage());
		}
		return false;
	}

	private void showNotificationsPlaceholder(UserAccount user) {
		System.out.println("Notifications for " + user.getEmail() + ":");
		// !!! Pending notifications table: notificationID, userID, category, message, seen, seenAt, createdAt
		System.out.println("(No notifications feature yet - pending schema update)");
	}

	private void viewUserHistoryPlaceholder() {
		String email = readLine("Enter email to view history: ");
		// !!! Pending dedicated session/device tracking table for accurate per-user history.
		System.out.println("History lookup for '" + email + "' is pending schema support.");
	}

	private void sendNotificationPlaceholder() {
		String targetEmail = readLine("Enter target email: ");
		String message = readLine("Enter notification message: ");
		// !!! Pending notifications table + sender tracking.
		System.out.println("Notification queued in simulation only for " + targetEmail + ": " + message);
	}

	private void manageDevicesPlaceholder() {
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
					showLoggedInUsers();
					break;
				case 2:
					setDeviceStatusForAdmin("available");
					break;
				case 3:
					setDeviceStatusForAdmin("offline");
					break;
				case 4:
					logOutAllUsers();
					break;
				case 5:
					return;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

	private void showLoggedInUsers() {
		String sql = "SELECT u.email, u.name, 'room259' AS room, r.sessionlogin " +
				"FROM room259 r JOIN users u ON u.userID = r.userID WHERE r.sessionlogout IS NULL " +
				"UNION ALL " +
				"SELECT u.email, u.name, 'room260' AS room, r.sessionlogin " +
				"FROM room260 r JOIN users u ON u.userID = r.userID WHERE r.sessionlogout IS NULL " +
				"ORDER BY room, sessionlogin";
		try (Connection conn = FormConnection.connectDb(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
			System.out.println("\nUsers currently logged into devices:");
			boolean found = false;
			while (rs.next()) {
				found = true;
				System.out.println("- " + rs.getString("email") + " (" + rs.getString("name") + ") in " + rs.getString("room") + " since " + rs.getString("sessionlogin"));
			}
			if (!found) {
				System.out.println("No active sessions.");
			}
		} catch (Exception e) {
			System.out.println("Error reading active sessions: " + e.getMessage());
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

	private void logOutAllUsers() {
		try (Connection conn = FormConnection.connectDb(); Statement stmt = conn.createStatement()) {
			conn.setAutoCommit(false);
			int room259Rows = stmt.executeUpdate("UPDATE room259 SET sessionlogout = NOW(), currentOccupancy = 0 WHERE sessionlogout IS NULL");
			int room260Rows = stmt.executeUpdate("UPDATE room260 SET sessionlogout = NOW(), currentOccupancy = 0 WHERE sessionlogout IS NULL");
			stmt.executeUpdate("UPDATE pc SET status = 'available' WHERE status = 'in_use'");
			stmt.executeUpdate("UPDATE printer SET status = 'available' WHERE status = 'in_use'");
			stmt.executeUpdate("UPDATE printer3d SET status = 'available' WHERE status = 'in_use'");
			conn.commit();
			System.out.println("Logged out all users. room259 rows updated: " + room259Rows + ", room260 rows updated: " + room260Rows);
		} catch (Exception e) {
			System.out.println("Error logging out all users: " + e.getMessage());
		}
	}

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
