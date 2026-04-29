package src.notification;

public class Notification {
    private final int id;
    private final boolean status;

    public Notification(int id, boolean status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public boolean getStatus() {
        return status;
    }
}
