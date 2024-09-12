#!/bin/bash
#has to use Unix line encoding to execute properly, the commands will be visible in database log when executed
set -e

psql -v ON_ERROR_STOP=1 --username postgres --dbname postgres <<-EOSQL
    CREATE USER admin WITH ENCRYPTED PASSWORD 'admin';
    CREATE DATABASE db;
    GRANT ALL PRIVILEGES ON DATABASE db TO admin;
	\connect db
  CREATE SCHEMA wiki;
  grant usage on schema wiki to admin;
  grant create on schema wiki to admin;
	\exit
EOSQL