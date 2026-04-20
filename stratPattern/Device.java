package stratPattern;

public class Device {

	// ---Public Enum---
	public enum types {COMPUTER, PRINTER, PRINTER3D}	// To access these class, do Device.types.COMPUTER

	// ---Attributes---
	protected int id;
	private types type;	
	public boolean status;	// true=available, false=inUse
	// For the strategy pattern
	private StatusStrategy statusStrategy;

	// Constructor
	public Device(int id, types type, boolean inUse) {
		this.id = id;
		this.type = type;
		this.status = inUse;
		setStatusStrategy();
	}
	public Device(int id, types type) {
		this.id = id;
		this.type = type;
		this.status = true;
		setStatusStrategy();
	}
	
	// ---Strategy Pattern---
	// Set device getStatus() stragegy
	public void setStatusStrategy() {
		switch (this.type) {
			case COMPUTER:
				this.statusStrategy = new ComputerStrategy();
				break;
			case PRINTER:
				this.statusStrategy = new Printer3DStrategy();
				break;
			case PRINTER3D:
				this.statusStrategy = new PrinterStrategy();
				break;
			default:
				break;
		}
	}
	// returns the status of the current device
	public boolean getStatus() {
		return statusStrategy.statusStrategy(this.id);
	}
	
	// ---Getters---
	public int getId() {
		return id;
	}
	public types getType() {
		return type;
	}
	public StatusStrategy getStatusStrategy() {
		return statusStrategy;
	}

	// ---Setters---
	public void setId(int id) {
		this.id = id;
	}
	public void setType(types type) {
		this.type = type;
	}
	public void setStatus(boolean inUse) {
		this.status = inUse;
	}

}
