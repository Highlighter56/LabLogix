package stratPattern;

public class ComputerStrategy implements StatusStrategy{
	// ?? How should this work?
	// !! Have this class contact the OccupancyTracker, and get the status, then return it
	public boolean statusStrategy(int id) {
		return true;
	}
}
