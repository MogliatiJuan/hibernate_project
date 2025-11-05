-- Script to create the database for Hibernate Project
-- Run this script in MySQL before starting the application

CREATE DATABASE IF NOT EXISTS hibernate_test 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Verify the database was created
SHOW DATABASES LIKE 'hibernate_test';

