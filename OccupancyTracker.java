import java.util.Date;
import java.sql.*;

import Test.TempUserAccount;

public class OccupancyTracker { 
	
	// ---Attributes---
	private Date timeStamp;
	private String action; 				
	private TempUserAccount person;		// Eventualy make this UserAccount
										
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
	/*
	Data Recorded: 
	- userID
	- timeStamp
	 - action (login or logout ??)
	 - on what device
	 - what room (technicaly not needed cause devices have there own room id, but I feel like it makes sense to have it directly)
	Where in the database would this get stored? 
	 */
	public void recordLogin(TempUserAccount user, Date timeStamp, String action, Device device) {
		Connection conn;
		Statement stmt;
		String strSelect;
		ResultSet rset;
		try {
			// Form Connection
			stmt = FormConnection.connect();
			
			// Modify the Device Table : Status of the Device
			Device.types type = device.getType();
			// Based on the type of device, go into that table, then modify (based on device id) that row/device's status to false/now in use
			String fieldName="";
			switch (type) {
				case Device.types.COMPUTER:
					fieldName = "pcID";
					break;
				case Device.types.PRINTER:
					fieldName = "printerID";
					break;
				case Device.types.PRINTER3D:
					fieldName = "printer3dID";
					break;
				default:
					strSelect = "";
					System.exit(0);
					break;
			}

			// !!! For enums in mysql, use single quotes

			// ?? How do you change something in the database? Like Im not trying to add a row or pull it, I need to modify an existing row. What does a querry that does that look like?...
			strSelect = "somethign"+fieldName+"somethign more"; // Have this be the statment 
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
