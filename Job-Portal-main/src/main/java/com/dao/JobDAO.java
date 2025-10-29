package com.dao;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.entity.Jobs;

public class JobDAO {

	private Connection conn;

	public JobDAO(Connection conn) {
		super();
		this.conn = conn;
	}

	public boolean addJobs(Jobs j) {

		boolean f = false;
		try {
			// column names/order in DB: id, title, description, category, location, status, pdate
			String sql = "insert into jobs(title, description, category, location, status) values(?, ?, ?, ?, ?)";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, j.getTitle());
				ps.setString(2, j.getDescription());
				ps.setString(3, j.getCategory());
				ps.setString(4, j.getLocation());
				ps.setString(5, j.getStatus());

				int i = ps.executeUpdate();
				if (i == 1) f = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public List<Jobs> getAllJobs() {

		List<Jobs> list = new ArrayList<Jobs>();
		Jobs j = null;

		try {
			String sql = "select * from jobs order by id desc";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sql);
			 	 java.sql.ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					j = new Jobs();
					j.setId(rs.getInt("id"));
					j.setTitle(rs.getString("title"));
					j.setDescription(rs.getString("description"));
					j.setCategory(rs.getString("category"));
					j.setLocation(rs.getString("location"));
					j.setStatus(rs.getString("status"));
					j.setPdate(rs.getString("pdate"));
					list.add(j);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public Jobs getJobById(int id) {

		Jobs j = null;

		try {
			String sql = "select * from jobs where id=?";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, id);
				try (java.sql.ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						j = new Jobs();
						j.setId(rs.getInt("id"));
						j.setTitle(rs.getString("title"));
						j.setDescription(rs.getString("description"));
						j.setCategory(rs.getString("category"));
						j.setLocation(rs.getString("location"));
						j.setStatus(rs.getString("status"));
						j.setPdate(rs.getString("pdate"));
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	public boolean updateJob(Jobs j) {

		boolean f = false;

		try {
			String sql = "update jobs set title=?, description=?, category=?, location=?, status=? where id = ?";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, j.getTitle());
				ps.setString(2, j.getDescription());
				ps.setString(3, j.getCategory());
				ps.setString(4, j.getLocation());
				ps.setString(5, j.getStatus());
				ps.setInt(6, j.getId());

				int i = ps.executeUpdate();
				if (i == 1) f = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public boolean deleteJob(int id) {

		boolean f = false;

		try {
			String sql = "delete from jobs where id = ?";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, id);

				int i = ps.executeUpdate();
				if (i == 1) f = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public List<Jobs> getAllJobsForUser() {

		List<Jobs> list = new ArrayList<Jobs>();
		Jobs j = null;

		try {
			String sql = "select * from jobs where status=? order by id desc";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, "Active");
				try (java.sql.ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						j = new Jobs();
						j.setId(rs.getInt("id"));
						j.setTitle(rs.getString("title"));
						j.setDescription(rs.getString("description"));
						j.setCategory(rs.getString("category"));
						j.setLocation(rs.getString("location"));
						j.setStatus(rs.getString("status"));
						j.setPdate(rs.getString("pdate"));
						list.add(j);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Jobs> getJobsOrLocationCategory(String cat, String loc) {

		List<Jobs> list = new ArrayList<Jobs>();
		Jobs j = null;

		try {
			String sql = "select * from jobs where status=? and (category=? or location=?) order by id desc";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, "Active");
				ps.setString(2, cat);
				ps.setString(3, loc);

				try (java.sql.ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						j = new Jobs();
						j.setId(rs.getInt("id"));
						j.setTitle(rs.getString("title"));
						j.setDescription(rs.getString("description"));
						j.setCategory(rs.getString("category"));
						j.setLocation(rs.getString("location"));
						j.setStatus(rs.getString("status"));
						j.setPdate(rs.getString("pdate"));
						list.add(j);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Jobs> getJobsAndLocationCategory(String cat, String loc) {

		List<Jobs> list = new ArrayList<Jobs>();
		Jobs j = null;

		try {
			String sql = "select * from jobs where status=? and category=? and location=? order by id desc";
			try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, "Active");
				ps.setString(2, cat);
				ps.setString(3, loc);

				try (java.sql.ResultSet rs = ps.executeQuery()) {
					while (rs.next()) {
						j = new Jobs();
						j.setId(rs.getInt("id"));
						j.setTitle(rs.getString("title"));
						j.setDescription(rs.getString("description"));
						j.setCategory(rs.getString("category"));
						j.setLocation(rs.getString("location"));
						j.setStatus(rs.getString("status"));
						j.setPdate(rs.getString("pdate"));
						list.add(j);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

}
