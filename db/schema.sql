-- SQL schema for Job-Portal application
-- Created: 2025-10-27

CREATE DATABASE IF NOT EXISTS job_portal CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE job_portal;

-- Application user (recommended)
CREATE USER IF NOT EXISTS 'job_user'@'localhost' IDENTIFIED BY 'job_password';
GRANT ALL PRIVILEGES ON job_portal.* TO 'job_user'@'localhost';
FLUSH PRIVILEGES;

-- Users table
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  qualification VARCHAR(255),
  role VARCHAR(50)
);

-- Jobs table
CREATE TABLE IF NOT EXISTS jobs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT,
  category VARCHAR(100),
  location VARCHAR(150),
  status VARCHAR(50),
  pdate VARCHAR(50)
);

-- Sample data
INSERT INTO users (name,email,password,qualification,role) VALUES
  ('Admin','admin@example.com','adminpass','MSc','admin'),
  ('User','user@example.com','userpass','BSc','user')
ON DUPLICATE KEY UPDATE email=email;

INSERT INTO jobs (title,description,category,location,status,pdate) VALUES
  ('Java Developer','Build webapps','IT','Remote','open','2025-10-27'),
  ('Frontend Dev','React and HTML','IT','Onsite','open','2025-10-25')
ON DUPLICATE KEY UPDATE title=title;
