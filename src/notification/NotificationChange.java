package src.notification;

public class NotificationChange {
    private final int id;
    private final boolean oldStatus;
    private final boolean newStatus;

    public NotificationChange(int id, boolean oldStatus, boolean newStatus) {
        this.id = id;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }

    public int getId() { return id; }
    public boolean getOldStatus() { return oldStatus; }
    public boolean getNewStatus() { return newStatus; }
}
