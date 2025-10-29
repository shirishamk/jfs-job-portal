package com.DB;

import java.sql.*;

public class DBConnect {

	private static Connection conn;
	
	public static Connection getConn() {
		
		try {
			// Reconnect if connection is null or closed. This handles DB restarts or timeouts.
			if (conn == null || conn.isClosed()) {
				Class.forName("com.mysql.cj.jdbc.Driver");
				// Use a dedicated application user for local development. If you created job_user/job_password
				// via the provided db/schema.sql, use those credentials. Adjust as needed.
				String url = "jdbc:mysql://localhost:3306/job_portal?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
				String user = System.getenv().getOrDefault("JOB_DB_USER", "job_user");
				String pass = System.getenv().getOrDefault("JOB_DB_PASS", "job_password");
				conn = DriverManager.getConnection(url, user, pass);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
}
