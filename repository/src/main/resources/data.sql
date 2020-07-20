CREATE TABLE certificate (
  certificate_id serial PRIMARY KEY,
  name varchar(60) NOT NULL,
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

CREATE TABLE certificate_m2m_tag (
    certificate_id int REFERENCES certificate (certificate_id) ON UPDATE CASCADE ON DELETE CASCADE,
    tag_id int REFERENCES tag (tag_id) ON DELETE CASCADE,
    CONSTRAINT certificate_tag_id PRIMARY KEY (certificate_id, tag_id)
);


CREATE OR REPLACE FUNCTION filter_by_text (text character varying)
  RETURNS TABLE (certificate_id   integer   -- also visible as OUT parameter inside function
               , name   varchar(60)
               , description varchar(300)
        , price numeric(8,2)
        , date_of_creation timestamp
        , date_of_modification timestamp
        , duration_in_days smallint) AS
$func$
BEGIN
   RETURN QUERY
   SELECT c.certificate_id, c.name, c.description, c.price, c.date_of_creation,
 c.date_of_modification, c.duration_in_days
  FROM certificate AS c
  WHERE c.description LIKE '%' || text || '%';
END
$func$  LANGUAGE plpgsql;


CREATE TABLE account (
  account_id serial PRIMARY KEY,
  login varchar(20) NOT NULL UNIQUE,
  role smallint NOT NULL
);

CREATE TABLE account_order (
  account_order_id serial PRIMARY KEY,
  price numeric(8, 2) NOT NULL DEFAULT 0,
  date_of_creation timestamp NOT NULL,
  account_id integer REFERENCES account (account_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE account_order_m2m_certificate (
    account_order_id int REFERENCES account_order (account_order_id) ON UPDATE CASCADE ON DELETE CASCADE,
    certificate_id int REFERENCES certificate (certificate_id),
    CONSTRAINT order_certificate_id PRIMARY KEY (account_order_id, certificate_id)
);

