package src.notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import src.FormConnection;

public class NotificationDAO {

    public Map<Integer, Boolean> fetchAllStatuses() {
        Map<Integer, Boolean> map = new HashMap<>();
        String sql = "SELECT notificationID, status FROM notification";
        try (Connection conn = FormConnection.connectDb();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("notificationID");
                boolean status = rs.getBoolean("status");
                map.put(id, status);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed reading notifications", e);
        }
        return map;
    }
}
