CREATE TABLE gift_certificate (
  gift_certificate_id serial PRIMARY KEY,
  name varchar(60) NOT NULL UNIQUE,
  description varchar(300),
  price numeric(8, 2) NOT NULL DEFAULT 0,
  date_of_creation timestamp NOT NULL,
  date_of_modification timestamp,
  duration_in_days smallint NOT NULL DEFAULT 5
);

CREATE TABLE tag (
  tag_id serial PRIMARY KEY,
  name varchar(35) NOT NULL UNIQUE
);

CREATE TABLE gift_certificate_m2m_tag (
    gift_certificate_id int REFERENCES gift_certificate (gift_certificate_id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id int REFERENCES tag (tag_id) ON DELETE CASCADE,
    CONSTRAINT certificate_tag_id PRIMARY KEY (gift_certificate_id, tag_id)
);


CREATE OR REPLACE FUNCTION filter_by_text (text character varying)
  RETURNS TABLE (gift_certificate_id   integer   -- also visible as OUT parameter inside function
               , name   varchar(60)
               , description varchar(300)
				, price numeric(8,2)
				, date_of_creation timestamp
				, date_of_modification timestamp
				, duration_in_days smallint) AS
$func$
BEGIN
   RETURN QUERY
   SELECT gc.gift_certificate_id, gc.name, gc.description, gc.price, gc.date_of_creation,
 gc.date_of_modification, gc.duration_in_days
	FROM gift_certificate AS gc
	WHERE gc.description LIKE '%' || text || '%';
END
$func$  LANGUAGE plpgsql;

