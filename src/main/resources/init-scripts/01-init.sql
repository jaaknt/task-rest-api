CREATE SCHEMA IF NOT EXISTS eagle AUTHORIZATION root;

CREATE USER app WITH PASSWORD 'app';

GRANT USAGE ON SCHEMA eagle TO app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA eagle TO app;
GRANT SELECT ON ALL SEQUENCES IN SCHEMA eagle TO app;

ALTER DEFAULT PRIVILEGES IN SCHEMA eagle GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO app;
ALTER DEFAULT PRIVILEGES IN SCHEMA eagle GRANT SELECT ON SEQUENCES TO app;


