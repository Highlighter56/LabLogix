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
        DatabaseMetaData md = conn.getMetaData();
        try (ResultSet rs = md.getColumns(null, null, table, "deviceID")) {
            if (!rs.next()) {
                System.out.println("Adding deviceID/deviceType to " + table);
                try (Statement s = conn.createStatement()) {
                    s.executeUpdate("ALTER TABLE " + table + " ADD COLUMN deviceID INT NULL, ADD COLUMN deviceType VARCHAR(32) NULL");
                }
            } else {
                System.out.println(table + " already has deviceID column");
            }
        }
    }
}
