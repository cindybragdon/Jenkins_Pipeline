DROP SCHEMA if exists ro;

CREATE SCHEMA ro;


CREATE TABLE rocket (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    sorte VARCHAR(40) NOT NULL 
);


INSERT INTO rocket (id, name, sorte)
VALUES (1, 'fusee1', 'fusee'), 
(2, 'balai', 'balai');