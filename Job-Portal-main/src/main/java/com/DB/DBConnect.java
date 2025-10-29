package com.DB;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnect {

	// Robust connection provider:
	// 1. Try container JNDI lookup for `java:comp/env/jdbc/job_portal`.
	//    If the bound DataSource implements javax.sql.DataSource, use it.
	//    Otherwise attempt to call getConnection() via reflection (handles jakarta.DataSource).
	// 2. Fallback to DriverManager with local dev credentials.
	public static Connection getConn() {
		// Try JNDI first
		try {
			InitialContext ctx = new InitialContext();
			Object dsObj = ctx.lookup("java:comp/env/jdbc/job_portal");
			if (dsObj != null) {
				// If it's a javax.sql.DataSource (typical on older containers) use it
				if (dsObj instanceof DataSource) {
					try {
						return ((DataSource) dsObj).getConnection();
					} catch (Exception e) {
						System.err.println("DBConnect: javax DataSource getConnection failed: " + e.getMessage());
					}
				}

				// Otherwise try reflection (handles jakarta.sql.DataSource or other implementations)
				try {
					Method m = dsObj.getClass().getMethod("getConnection");
					Object conn = m.invoke(dsObj);
					if (conn instanceof Connection) {
						return (Connection) conn;
					}
				} catch (NoSuchMethodException nsme) {
					System.err.println("DBConnect: DataSource has no getConnection method: " + nsme.getMessage());
				} catch (Exception re) {
					System.err.println("DBConnect: reflection getConnection failed: " + re.getMessage());
				}
			} else {
				System.out.println("DBConnect: DataSource lookup returned null");
			}
		} catch (NamingException ne) {
			System.err.println("DBConnect JNDI lookup failed: " + ne.getMessage());
		} catch (Exception e) {
			System.err.println("DBConnect JNDI error: " + e.getMessage());
			e.printStackTrace();
		}

		// Fallback to DriverManager (development fallback)
		try {
			// Development defaults - change if your DB uses different creds
			String url = "jdbc:mysql://localhost:3306/job_portal?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC";
			String user = "job_user";
			String pass = "job_password";
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			System.err.println("DBConnect DriverManager fallback failed: " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

}
