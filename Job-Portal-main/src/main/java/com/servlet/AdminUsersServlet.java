package com.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.DB.DBConnect;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Development-only users listing. Mapped under /dev to avoid being accidentally visited.
@WebServlet("/dev/admin/users")
public class AdminUsersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Restrict to localhost for safety in development
        String remote = req.getRemoteAddr();
        if (!"127.0.0.1".equals(remote) && !"::1".equals(remote) && !"0:0:0:0:0:0:0:1".equals(remote)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return;
        }

        resp.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><head><title>Users</title></head><body>");
            out.println("<h2>Users (development only)</h2>");

            Connection conn = null;
            try {
                conn = DBConnect.getConn();
                if (conn == null) {
                    out.println("<p><b>Error:</b> Database connection unavailable.</p>");
                    out.println("</body></html>");
                    return;
                }

                String sql = "SELECT id, name, email, role FROM users";
                try (PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {

                    out.println("<table border='1' cellpadding='6'><tr><th>id</th><th>name</th><th>email</th><th>role</th></tr>");
                    while (rs.next()) {
                        out.printf("<tr><td>%d</td><td>%s</td><td>%s</td><td>%s</td></tr>", rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                    }
                    out.println("</table>");

                } catch (Exception e) {
                    out.println("<pre>DB error: " + e.getMessage() + "</pre>");
                }

            } catch (Exception e) {
                out.println("<pre>DB error: " + e.getMessage() + "</pre>");
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (Exception ignore) {
                        // ignore close errors
                    }
                }
            }

            out.println("</body></html>");
        }
    }

}
