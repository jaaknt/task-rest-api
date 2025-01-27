BEGIN;

SET SEARCH_PATH = eagle;

CREATE TABLE task (
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(255) NOT NULL,
    description   VARCHAR(255) NOT NULL,
    status        VARCHAR(32) NOT NULL,
    due_date      TIMESTAMP,
    user_id       BIGINT,
    created_at    TIMESTAMP NOT NULL,
    modified_at   TIMESTAMP NOT NULL
);

ALTER TABLE task
    ADD CONSTRAINT fk_user_id
    FOREIGN KEY (user_id)
    REFERENCES "user"(id);

CREATE TRIGGER task_created_modified_update_trg
    BEFORE INSERT OR UPDATE
       ON task
       FOR EACH ROW
       EXECUTE FUNCTION created_modified_update_trg();

COMMIT;
