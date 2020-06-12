CREATE TABLE gift_certificate (
  gift_certificate_id serial PRIMARY KEY,
  name varchar(60) NOT NULL UNIQUE,
  description varchar(300),
  price numeric(5, 2) NOT NULL DEFAULT 0,
  date_of_creation timestamp NOT NULL DEFAULT CURRENT_DATE,
  date_of_modification timestamp,
  duration_in_days smallint NOT NULL DEFAULT 5
);

CREATE TABLE tag (
  tag_id serial PRIMARY KEY,
  name varchar(35) NOT NULL UNIQUE
);

CREATE TABLE gift_certificate_m2m_tag (
    gift_certificate_id int REFERENCES gift_certificate (gift_certificate_id) ON UPDATE CASCADE ON DELETE CASCADE,
  tag_id int REFERENCES tag (tag_id) ON UPDATE CASCADE,
  CONSTRAINT certificate_tag_id PRIMARY KEY (gift_certificate_id, tag_id)
);