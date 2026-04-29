package src.notification;

public class ConsoleNotificationListener implements NotificationObserver {
    @Override
    public void onNotificationChange(NotificationChange change) {
        System.out.println("[Notification] id=" + change.getId() + " changed: " + change.getOldStatus() + " -> " + change.getNewStatus());
    }
}
