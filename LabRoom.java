import java.sql.*;
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
	
	// When want available = true, return only available devices. Else return all devices
	private int[] devicesHelper(boolean wantAvailable) {
		String onlyAvailable = "";
		if(wantAvailable)
			onlyAvailable = "where status = \"available\"";

		int pcCount=0, printerCount=0, printer3dCount=0, count=0, i=0;
		int[] devices = new int[count];
		try {
			// Form Connection
			Statement stmt = FormConnection.connect();
			
			// Get Device Count
			// PCs
			String strSelect = "select count(*) from pc"+onlyAvailable;
			ResultSet rset = stmt.executeQuery(strSelect);
			rset.next();
			pcCount = rset.getInt("count(*)");
			count += pcCount;
			// Printers
			if (this.id==259) {
				strSelect = "select count(*) from printer"+onlyAvailable;
				rset = stmt.executeQuery(strSelect);
				rset.next();
				
				printerCount = rset.getInt("count(*)");
				count += printerCount;
			}
			// Printer3Ds
			if (this.id==260) {
				strSelect = "select count(*) from printer3d"+onlyAvailable;
				rset = stmt.executeQuery(strSelect);
				rset.next();
				printer3dCount = rset.getInt("count(*)");
				count += printer3dCount;
			}
			
			// Add IDs to array
			// Get PC IDs
			if (pcCount!=0) {
				strSelect = "select pcID from pc"+onlyAvailable;
				rset = stmt.executeQuery(strSelect);
				// add pc ids to array
				while(rset.next()) {
					devices[i++] = rset.getInt("pcID");
				}
			}
			// Get Printer IDs
			if (printerCount!=0) {
				strSelect = "select printerID from printer"+onlyAvailable;
				rset = stmt.executeQuery(strSelect);
				// add printer ids to array
				while(rset.next()) {
					devices[i++] = rset.getInt("printerID");
				}
			}
			// Get Printer3d IDs
			if (pcCount!=0) {
				strSelect = "select printer3dID from printer3d"+onlyAvailable;
				rset = stmt.executeQuery(strSelect);
				// add pc ids to array
				while(rset.next()) {
					devices[i++] = rset.getInt("printer3dID");
				}
			}
		} catch(Exception e) {
			System.out.println(e);
		}
		return devices;
	}
		
}
// printer only in 259
// printer3d only in 260