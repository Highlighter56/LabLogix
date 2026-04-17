// This is an import statment that allows all the strategy pattern stuff to be contained in a seperate folder, but you can still access Device
import stratPattern.Device;

// ?? Our UML says OccupancyTracker should extend LabRoom, I think this should be the other way around
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
	public void device() {

	}
	// ?? Should this return a list of device IDs? Should this return the num of free devices? | If this just returns a num, should there be a way to speify what device you wanna know if its free?
	public Device[] availablDevices() {
		return new Device[5];
		// Pull the list of computers for this LabRoom
		// Then from this list count how many are in use
		// Return
	}

	
}
