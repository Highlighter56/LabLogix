package src;

import src.notification.NotificationService;
import src.notification.ConsoleNotificationListener;

public class Main {
	public static void main(String[] args) {
		NotificationService notificationService = new NotificationService(5);
		notificationService.register(new ConsoleNotificationListener());
		notificationService.start();

		InteractiveTerminal terminal = new InteractiveTerminal();
		try {
			terminal.run();
		} finally {
			notificationService.stop();
		}
	}
}
