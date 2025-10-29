package com.dao;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

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
			// Use the correctly spelled 'description' column. If your DB still has 'discription', keep that column
			// or migrate it to 'description'. Using column names explicitly avoids issues with column order.
			String sql = "insert into jobs(title, description, category, status, location) values(?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, j.getTitle());
			ps.setString(2, j.getDescription());
			ps.setString(3, j.getCategory());
			ps.setString(4, j.getStatus());
			ps.setString(5, j.getLocation());

			int i = ps.executeUpdate();

			if (i == 1)
				f = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public List<Jobs> getAllJobs() {

		List<Jobs> list = new ArrayList<Jobs>();
		Jobs j = null;

		try {
			String sql = "select id, title, description, category, location, status, pdate from jobs order by id desc";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				j = new Jobs();
				j.setId(rs.getInt("id"));
				j.setTitle(rs.getString("title"));
				j.setDescription(rs.getString("description"));
				j.setCategory(rs.getString("category"));
				j.setLocation(rs.getString("location"));
				j.setStatus(rs.getString("status"));
				Timestamp ts = rs.getTimestamp("pdate");
				j.setPdate(ts != null ? ts.toString() : rs.getString("pdate"));
				list.add(j);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public Jobs getJobById(int id) {

		Jobs j = null;

		try {
			String sql = "select id, title, description, category, location, status, pdate from jobs where id=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				j = new Jobs();
				j.setId(rs.getInt("id"));
				j.setTitle(rs.getString("title"));
				j.setDescription(rs.getString("description"));
				j.setCategory(rs.getString("category"));
				j.setLocation(rs.getString("location"));
				j.setStatus(rs.getString("status"));
				Timestamp ts = rs.getTimestamp("pdate");
				j.setPdate(ts != null ? ts.toString() : rs.getString("pdate"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}

	public boolean updateJob(Jobs j) {

		boolean f = false;

		try {
			String sql = "update jobs set title=?, description=?, category=?, status=?, location=? where id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, j.getTitle());
			ps.setString(2, j.getDescription());
			ps.setString(3, j.getCategory());
			ps.setString(4, j.getStatus());
			ps.setString(5, j.getLocation());
			ps.setInt(6, j.getId());

			int i = ps.executeUpdate();

			if (i == 1)
				f = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public boolean deleteJob(int id) {

		boolean f = false;

		try {
			String sql = "delete from jobs where id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);

			int i = ps.executeUpdate();

			if (i == 1)
				f = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}

	public List<Jobs> getAllJobsForUser() {

		List<Jobs> list = new ArrayList<Jobs>();
		Jobs j = null;

		try {
			String sql = "select id, title, description, category, location, status, pdate from jobs where status=? order by id desc";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, "Active");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				j = new Jobs();
				j.setId(rs.getInt("id"));
				j.setTitle(rs.getString("title"));
				j.setDescription(rs.getString("description"));
				j.setCategory(rs.getString("category"));
				j.setLocation(rs.getString("location"));
				j.setStatus(rs.getString("status"));
				Timestamp ts = rs.getTimestamp("pdate");
				j.setPdate(ts != null ? ts.toString() : rs.getString("pdate"));
				list.add(j);
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
			String sql = "select id, title, description, category, location, status, pdate from jobs where status=? and (category=? or location=?) order by id desc";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, "Active");
			ps.setString(2, cat);
			ps.setString(3, loc);
			
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				j = new Jobs();
				j.setId(rs.getInt("id"));
				j.setTitle(rs.getString("title"));
				j.setDescription(rs.getString("description"));
				j.setCategory(rs.getString("category"));
				j.setLocation(rs.getString("location"));
				j.setStatus(rs.getString("status"));
				Timestamp ts = rs.getTimestamp("pdate");
				j.setPdate(ts != null ? ts.toString() : rs.getString("pdate"));
				list.add(j);
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
			String sql = "select id, title, description, category, location, status, pdate from jobs where status=? and category=? and location=? order by id desc";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, "Active");
			ps.setString(2, cat);
			ps.setString(3, loc);
			
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				j = new Jobs();
				j.setId(rs.getInt("id"));
				j.setTitle(rs.getString("title"));
				j.setDescription(rs.getString("description"));
				j.setCategory(rs.getString("category"));
				j.setLocation(rs.getString("location"));
				j.setStatus(rs.getString("status"));
				Timestamp ts = rs.getTimestamp("pdate");
				j.setPdate(ts != null ? ts.toString() : rs.getString("pdate"));
				list.add(j);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

}
