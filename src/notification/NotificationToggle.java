package src.notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import src.FormConnection;

public class NotificationToggle {
    public static void main(String[] args) throws Exception {
        int id = 1;
        if (args.length > 0) {
            id = Integer.parseInt(args[0]);
        }
        try (Connection conn = FormConnection.connectDb()) {
            // toggle status
            String q = "UPDATE notification SET status = NOT status WHERE notificationID = ?";
            try (PreparedStatement ps = conn.prepareStatement(q)) {
                ps.setInt(1, id);
                int updated = ps.executeUpdate();
                System.out.println("Toggled notification " + id + ", rows=" + updated);
            }
        }
    }
}
