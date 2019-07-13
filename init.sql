-- Setup a user and a database that will be used by the application --
-- Run this script as someone with super user rights --
CREATE ROLE tvadmin WITH PASSWORD 'tvadmin';
ALTER ROLE tvadmin WITH LOGIN;
CREATE DATABASE tadvidya;
GRANT ALL PRIVILEGES ON DATABASE "tadvidya" TO tvadmin;
