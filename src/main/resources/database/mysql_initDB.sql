CREATE TABLE user
(
    id              INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name            VARCHAR(25),
    age             INT,
    isAdmin         BIT,
    createdDate     TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);