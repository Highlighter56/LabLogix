package stratPattern;

public class ComputerStrategy implements StatusStrategy{
	// ?? How should this work?
	// !! Have this class contact the OccupancyTracker, and get the status, then return it
	public boolean statusStrategy(int id) {
		return true;
	}
	/*
	The strategy pattern is supposed to be unique
	for each type of algorithm implemented. I dont
	know how to make a status algorith thats different
	for each device unless we start taking in more data.
	Like computer could be a simple db pull, which is 
	based on user login/out. Printer could be based on 
	sheets of paper left in the printer, or ink levels.
	3D printers could send a complexity tag or maybe a 
	time requirment based on the last print to determine
	when its ready. 
	Unless someone else has an idea to make these algorithms
	different, ill do this? and try to make sure these changes
	dont affect any other part of the code? 
	But how would I store these differnces? All the devicies 
	are just the same class, with a one variable differnece
	for type. Maybe I could split these up into many classes,
	and cause there still the same parent class it would still 
	count as strategy pattern, but im not sure...
	*/
}
