CREATE or REPLACE TRIGGER article_like_trigger
 BEFORE INSERT OR UPDATE OR DELETE
                  ON article_like
                      FOR EACH ROW
                      EXECUTE PROCEDURE article_like_trigger_function();
