package src.notification;

public class NotificationRunner {
    public static void main(String[] args) {
        NotificationService svc = new NotificationService(5);
        svc.register(new ConsoleNotificationListener());
        svc.start();

        System.out.println("Notification service started (press Ctrl+C to exit)");

        // Keep main thread alive
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
