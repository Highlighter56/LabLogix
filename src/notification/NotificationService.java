package src.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NotificationService {
    private final List<NotificationObserver> observers = new ArrayList<>();
    private final NotificationDAO dao = new NotificationDAO();
    private final NotificationPoller poller;
    private Thread pollerThread;

    public NotificationService(int pollIntervalSeconds) {
        this.poller = new NotificationPoller(this, dao, pollIntervalSeconds);
    }

    public void register(NotificationObserver observer) {
        synchronized (observers) {
            observers.add(observer);
        }
    }

    public void unregister(NotificationObserver observer) {
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    void notifyAllObservers(NotificationChange change) {
        synchronized (observers) {
            for (NotificationObserver o : observers) {
                try {
                    o.onNotificationChange(change);
                } catch (Exception e) {
                    System.err.println("Observer failed: " + e.getMessage());
                }
            }
        }
    }

    public void start() {
        pollerThread = new Thread(poller, "NotificationPoller");
        pollerThread.setDaemon(true);
        pollerThread.start();
    }

    public void stop() {
        try {
            poller.stop();
        } catch (Exception e) {
            // ignore
        }
        if (pollerThread != null) {
            pollerThread.interrupt();
        }
    }
}
