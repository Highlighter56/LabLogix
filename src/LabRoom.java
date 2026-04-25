package src;
import java.sql.*;
import java.util.ArrayList;
// Allows stratPattern to stay seperate
// import stratPattern.Device;

public class LabRoom {
	
	// ---Attributes---
	private int id;
	private int capacity;
	private int currentOccupancy;


	// ---Constructors---
	public LabRoom(int id, int capacity, int currentOccupancy) {
		this.id = id;
		this.capacity = capacity;
		this.currentOccupancy = currentOccupancy;
	}
	public LabRoom(int id, int capacity) {
		this.id = id;
		this.capacity = capacity;
	}

	// ---Getters---
	public int getId() {
		return id;
	}
	public int getCapacity() {
		return capacity;
	}
	public int getCurrentOccupancy() {
		return currentOccupancy;
	}
	
	// ---Setters---
	public void setId(int id) {
		this.id = id;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public void setCurrentOccupancy(int currentOccupancy) {
		this.currentOccupancy = currentOccupancy;
	}

	// ---Methods---
	// Returns a list of devices in the given room
	public int[] devices() {
		return devicesHelper(false);
	}

	// Returns a list of available devices in a given room
	public int[] availablDevices() {
		return devicesHelper(true);
	}

	// Kept for readability while preserving existing call sites.
	public int[] availableDevices() {
		return availablDevices();
	}
	
	// When want available = true, return only available devices. Else return all devices
	private int[] devicesHelper(boolean wantAvailable) {
		String statusFilter = wantAvailable ? " AND status = 'available'" : "";
		ArrayList<Integer> devices = new ArrayList<>();

		try (Statement stmt = FormConnection.connect()) {
			String pcRoomField = this.id == 259 ? "room259ID" : "room260ID";
			String strSelect = "SELECT pcID FROM pc WHERE " + pcRoomField + " IS NOT NULL" + statusFilter;
			try (ResultSet rset = stmt.executeQuery(strSelect)) {
				while(rset.next()) {
					devices.add(rset.getInt("pcID"));
				}
			}

			if (this.id == 259) {
				strSelect = "SELECT printerID FROM printer WHERE room259ID IS NOT NULL" + statusFilter;
				try (ResultSet rset = stmt.executeQuery(strSelect)) {
					while(rset.next()) {
						devices.add(rset.getInt("printerID"));
					}
				}
			}

			if (this.id == 260) {
				strSelect = "SELECT printer3dID FROM printer3d WHERE room260ID IS NOT NULL" + statusFilter;
				try (ResultSet rset = stmt.executeQuery(strSelect)) {
					while(rset.next()) {
						devices.add(rset.getInt("printer3dID"));
					}
				}
			}
		} catch(Exception e) {
			System.out.println(e);
		}

		int[] result = new int[devices.size()];
		for (int i = 0; i < devices.size(); i++) {
			result[i] = devices.get(i);
		}
		return result;
	}
		
}
// printer only in 259
// printer3d only in 260