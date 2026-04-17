import java.util.Date;

// ?? Our UML says OccupancyTracker should extend LabRoom, I think this should be the other way around
// ?? Does this class make sense?
public class OccupancyTracker { 
	
	// ---Attributes---
	private Date timeStamp;
	private String action; // ?? Should this be an enum?
	private TempUserAccount person;		// This only says temp becuase I didnt want to make an entire UserAccount or commit a UserAccount and then override something
	
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
	public void recordLogin(TempUserAccount user) {

	}
	public void recordLogout(TempUserAccount user) {

	}

}
