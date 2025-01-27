SET SEARCH_PATH = eagle;

CREATE FUNCTION created_modified_update_trg()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS $$
BEGIN
   IF TG_OP = 'INSERT' THEN
      NEW.created_at := NOW();
      NEW.modified_at := NOW();
   ELSIF TG_OP = 'UPDATE' THEN
      NEW.modified_at := NOW();
   END IF;

RETURN NEW;

END;
$$
@@