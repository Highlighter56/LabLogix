// This is an import statment that allows all the strategy pattern stuff to be contained in a seperate folder, but you can still access Device
import java.sql.*;

import stratPattern.Device;

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
	// ?? What does this method do? Does it show a list of devices? Does it add a device to the LabRoom? Something else?
	// !! Return a list of all device ids that are associated with the room
	public Device[] devices(String db_url, String db_user, String db_password) {

	}
	// ?? Should this return a list of device IDs? Should this return the num of free devices? | If this just returns a num, should there be a way to speify what device you wanna know if its free?
	// !! List of device ids
	public Device[] availablDevices(String db_url, String db_user, String db_password) {
		return new Device[5];
		// Pull the list of computers for this LabRoom
		// Then from this list count how many are in use
		// Return
	}

	
}
// printer only in 259
// printer3d only in 260
