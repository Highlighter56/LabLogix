package src.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import src.FormConnection;

public class SchemaPatcher {
    public static void main(String[] args) {
        try (Connection conn = FormConnection.connectDb()) {
            patchRoom(conn, "room259");
            patchRoom(conn, "room260");
            System.out.println("Schema patching complete.");
        } catch (Exception e) {
            System.err.println("Schema patch failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void patchRoom(Connection conn, String table) throws Exception {
        // No longer adding deviceID/deviceType to room tables. Ensure device tables contain userID column instead.
        DatabaseMetaData md = conn.getMetaData();
        String[] deviceTables = {"pc", "printer", "printer3d"};
        for (String dev : deviceTables) {
            try (ResultSet rs = md.getColumns(null, null, dev, "userID")) {
                if (!rs.next()) {
                    System.out.println("Adding userID to device table: " + dev);
                    try (Statement s = conn.createStatement()) {
                        s.executeUpdate("ALTER TABLE " + dev + " ADD COLUMN userID INT NULL");
                    }
                } else {
                    System.out.println(dev + " already has userID column");
                }
            }
        }
    }
}
