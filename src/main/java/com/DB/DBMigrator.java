package com.DB;

import com.DB.DBConnect;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

public class DBMigrator {

    public static void main(String[] args) {
        Connection conn = null;
        Statement st = null;
        try {
            conn = DBConnect.getConn();
            if (conn == null) {
                System.err.println("DBMigrator: unable to obtain DB connection. Aborting migration.");
                System.exit(1);
            }
            st = conn.createStatement();

            System.out.println("Starting DB migration: ensure 'description' column exists and copy data from 'discription' if present");

            // Check whether 'description' column exists using information_schema; ALTER only if missing.
            String checkCol = "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'jobs' AND column_name = 'description'";
            try (ResultSet crs = st.executeQuery(checkCol)) {
                boolean needAdd = true;
                if (crs.next()) {
                    needAdd = crs.getInt(1) == 0;
                }
                if (needAdd) {
                    st.executeUpdate("ALTER TABLE jobs ADD COLUMN description TEXT");
                    System.out.println("Added 'description' column to jobs table.");
                } else {
                    System.out.println("'description' column already exists.");
                }
            }

            String update = "UPDATE jobs SET description = discription WHERE (description IS NULL OR description = '') AND (discription IS NOT NULL AND discription <> '')";
            int updated = st.executeUpdate(update);
            System.out.println("Rows updated (copied from discription -> description): " + updated);

            // report totals
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM jobs WHERE description IS NOT NULL AND description <> ''")) {
                if (rs.next()) {
                    System.out.println("Jobs with non-empty description: " + rs.getInt(1));
                }
            }

            System.out.println("DB migration completed.");

        } catch (Exception e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (st != null) st.close();
            } catch (Exception ignore) {}
            try {
                if (conn != null) conn.close();
            } catch (Exception ignore) {}
        }
    }
}
