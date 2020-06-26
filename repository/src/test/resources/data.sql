DROP TABLE IF EXISTS tag;

CREATE TABLE tag (
  tag_id serial PRIMARY KEY,
  name varchar(35) NOT NULL UNIQUE
);

INSERT INTO tag (tag_id, name) VALUES
  (1, 'books'),
  (2, 'magazines')

