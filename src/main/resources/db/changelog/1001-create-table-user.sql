BEGIN;

SET SEARCH_PATH = eagle;

CREATE TABLE "user" (
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    enabled       BOOLEAN NOT NULL,
    created_at    TIMESTAMP NOT NULL,
    modified_at   TIMESTAMP NOT NULL
);


CREATE TRIGGER user_created_modified_update_trg
    BEFORE INSERT OR UPDATE
       ON "user"
       FOR EACH ROW
       EXECUTE FUNCTION created_modified_update_trg();

COMMIT;
