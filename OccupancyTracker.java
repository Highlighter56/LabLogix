import java.util.Date;
import stratPattern.Device;
import java.sql.*;

// ?? Even though the database says OccupancyTracker should be a child/have a relationship to LabRoom, Im going to treat them completly seperatly cause I think it makes more sense ?..
public class OccupancyTracker { 
	
	// ---Attributes---
	private Date timeStamp;
	private String action; 				// ?? What is this for? In my head, its login or logout. But the database has nothing that talks about action.
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
	// ** DEF does not work
	// ?? What is the differnce between my OccupancyTracker recordLogin() and UserAccount login()
	// *** login() and logout() should check the current state, to validate if someone can login/out. If they can, then call this method and store it.
	/*
	Sends login data to the database. 
	Data Recorded: 
	- userID
	- timeStamp
	 - action (login or logout ??)
	 - on what device
	 - what room (technicaly not needed cause devices have there own room id, but I feel like it makes sense to have it directly)
	Where in the database would this get stored? 
	 */
	// ?? Its kind of annoyign to pass the three db parameters everytime, is there a way around this
	public void recordLogin(String db_url, String db_user, String db_password, TempUserAccount user, Date timeStamp, String action, Device device) {
		Connection conn;
		Statement stmt;
		String strSelect;
		ResultSet rset;
		try {
			// Form Connection
			conn = DriverManager.getConnection(db_url, db_user, db_password);
			stmt = conn.createStatement();

			/*
			The only tables that seem to need to be modified would be
			the room tables, and the device tables. Both can be accessed
			through the Device object passed.
			 */
			// Modify the Device Table : Status of the Device
			Device.types type = device.getType();
			// Based on the type of device, go into that table, then modify (based on device id) that row/device's status to false/now in use
			String varName="";
			switch (type) {
				case Device.types.COMPUTER:
					varName = "pcID";
					break;
				case Device.types.PRINTER:
					varName = "printerID";
					break;
				case Device.types.PRINTER3D:
					varName = "printer3dID";
					break;
				default:
					strSelect = "";
					System.out.println("Something went wrong...");
					// It shouldnt be possible for a device to not have a type
					System.exit(0);
					break;
			}
			// ?? How do you change something in the database? Like Im not trying to add a row or pull it, I need to modify an existing row. What does a querry that does that look like?...
			strSelect = "somethign"+varName+"somethign more"; // Have this be the statment 
			rset = stmt.executeQuery(strSelect);

			// Get RoomID from Device

			// in that table, set all values

		} catch (Exception e) {
			System.out.println(e);
		}

	}
	// ** Prob have a helper method sense this is so close to login, except with one check prior and action of logout instead of login
	public void recordLogout(TempUserAccount user, Date timeStamp, String action, Device device) {

	}

}
