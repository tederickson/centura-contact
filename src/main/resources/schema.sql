CREATE TABLE CONTACT (
    id         INTEGER     NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(60) NOT NULL,
    last_name  VARCHAR(60) NOT NULL,
    phone      VARCHAR(10) NOT NULL,
    
    PRIMARY KEY (id)
);