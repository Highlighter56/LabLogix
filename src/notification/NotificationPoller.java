package src.notification;

import java.util.HashMap;
import java.util.Map;

public class NotificationPoller implements Runnable {
    private final NotificationService service;
    private final NotificationDAO dao;
    private final int intervalSeconds;
    private volatile boolean running = true;
    private Map<Integer, Boolean> lastSnapshot = new HashMap<>();

    public NotificationPoller(NotificationService service, NotificationDAO dao, int intervalSeconds) {
        this.service = service;
        this.dao = dao;
        this.intervalSeconds = intervalSeconds;
        this.lastSnapshot = dao.fetchAllStatuses();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(intervalSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            try {
                Map<Integer, Boolean> current = dao.fetchAllStatuses();
                for (Map.Entry<Integer, Boolean> e : current.entrySet()) {
                    int id = e.getKey();
                    boolean status = e.getValue();
                    boolean previous = lastSnapshot.getOrDefault(id, !status);
                    if (previous != status) {
                        NotificationChange change = new NotificationChange(id, previous, status);
                        service.notifyAllObservers(change);
                    }
                }
                lastSnapshot = current;
            } catch (Exception e) {
                System.err.println("NotificationPoller error: " + e.getMessage());
            }
        }
    }

    public void stop() {
        running = false;
    }
}
