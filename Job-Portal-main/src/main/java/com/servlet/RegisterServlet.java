package com.servlet;

import java.io.IOException;

import com.dao.UserDAO;
import com.entity.User;
import com.DB.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		try {
			String name = req.getParameter("fullName");
			String qua = req.getParameter("qualification");
			String email = req.getParameter("email");
			String ps = req.getParameter("password");
			
			try (java.sql.Connection conn = DBConnect.getConn()) {
				if (conn == null) {
					HttpSession session = req.getSession();
					session.setAttribute("succMsg", "Database connection not available. Contact admin.");
					resp.sendRedirect("signup.jsp");
					return;
				}

				UserDAO dao = new UserDAO(conn);
				User u = new User(name, email, ps, qua, "user");

				boolean f = dao.addUser(u);

			HttpSession session = req.getSession();

				if (f) {
					session.setAttribute("succMsg", "Registration Successful");
				} else {
					// Better error messaging for users while keeping details in server logs
					session.setAttribute("succMsg", "Registration failed. Please try again or contact admin.");
				}
				resp.sendRedirect("signup.jsp");
			}

		} catch (Exception e) {
			// Log and show user-friendly message
			e.printStackTrace();
			HttpSession session = req.getSession();
			session.setAttribute("succMsg", "Server error occurred during registration. Check logs.");
			resp.sendRedirect("signup.jsp");
		}
		
	}

}
