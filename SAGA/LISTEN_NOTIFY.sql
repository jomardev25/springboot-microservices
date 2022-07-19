CREATE OR REPLACE FUNCTION notify_order_saved()
RETURNS trigger AS $$
BEGIN
  PERFORM pg_notify(
    'ORDER_SAVED',
    json_build_object(
      'operation', TG_OP,
      'record', row_to_json(NEW)
    )::text
  );

  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER order_saved_trigger
AFTER INSERT OR UPDATE
ON orders
FOR EACH ROW
EXECUTE PROCEDURE notify_order_saved()


CREATE FUNCTION notify_order_saved()
    RETURNS TRIGGER
AS $$
BEGIN 
   PERFORM pg_notify('ORDER_SAVED',  row_to_json(NEW)::text);
   RETURN NULL;
END;

CREATE TRIGGER order_saved_trigger
   AFTER INSERT OR UPDATE
   ON orders
   FOR EACH ROW
    EXECUTE PROCEDURE notify_order_saved();