package com.DB;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // no-op
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            // Deregister JDBC drivers to avoid memory leaks on redeploy
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            // Shutdown MySQL abandoned connection cleanup thread (safe to call)
            try {
                AbandonedConnectionCleanupThread.checkedShutdown();
            } catch (Throwable t) {
                // ignore
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
