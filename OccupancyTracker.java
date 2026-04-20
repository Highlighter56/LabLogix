import java.util.Date;

// ?? Our UML says OccupancyTracker should extend LabRoom, I think this should be the other way around
// ?? Does this class make sense?
// !! OccupancyTracker is the calss that goes from java to the database. No class except this class should queerry the database
public class OccupancyTracker { 
	
	// ---Attributes---
	private Date timeStamp;
	private String action; // ?? Should this be an enum? // !! Could be an enum? Check the database and make it work
	private TempUserAccount person;		// This only says temp becuase I didnt want to make an entire UserAccount or commit a UserAccount and then override something
										// Eventualy make this UserAccount
	// ---Constructors---
	public OccupancyTracker(Date timeStamp, String action, TempUserAccount person) {
		this.timeStamp = timeStamp;
		this.action = action;
		this.person = person;
	}

	// ---Getters---
	public Date getTimeStamp() {
		return timeStamp;
	}
	public String getAction() {
		return action;
	}
	public TempUserAccount getPerson() {
		return person;
	}

	// ---Setters---
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setPerson(TempUserAccount person) {
		this.person = person;
	}

	// ---Methods---
	// ?? Both of these methods should interact with the database, and then they need to take a paremeter of who is logging out, and then this needs to update the status of what device they were logged into
	// !! Look at the database and I have free rain with this as long as it makes sense
	public void recordLogin(TempUserAccount user) {

	}
	public void recordLogout(TempUserAccount user) {

	}

}
