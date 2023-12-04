CREATE DATABASE books;
USE books;

CREATE TABLE books (
  id INT AUTO_INCREMENT,
  title VARCHAR(255),
  author VARCHAR(255),
  rating FLOAT(2,1),
  voters INT,
  price FLOAT(255,2),
  currency VARCHAR(255),
  description VARCHAR(255),
  publisher VARCHAR(255),
  page_count INT,
  genres VARCHAR(255),
  ISBN INT,
  language VARCHAR(255),
  published_date DATE,
  PRIMARY KEY(id)
);

LOAD DATA LOCAL INFILE 'books.csv'
  INTO TABLE books
  FIELDS TERMINATED BY '|'
  LINES TERMINATED BY '\n'
  IGNORE 1 ROWS;