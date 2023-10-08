CREATE TABLE users (
    username VARCHAR(50) NOT NULL,
    fullname VARCHAR(50) NOT NULL,
    phonenum VARCHAR(10) NOT NULL,
    address VARCHAR(256) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE user_roles (
    user_role_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    username VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_role_id),
    FOREIGN KEY (username) REFERENCES users(username)
);

INSERT INTO users VALUES ('keith', 'Keith Lee', '99999999', 'HK', '{noop}keithpw');
INSERT INTO user_roles(username, role) VALUES ('keith', 'ROLE_STUDENT');
INSERT INTO user_roles(username, role) VALUES ('keith', 'ROLE_LECTURER');

INSERT INTO users VALUES ('john', 'john Tsang', '99999999', 'HK', '{noop}johnpw');
INSERT INTO user_roles(username, role) VALUES ('john', 'ROLE_LECTURER');

INSERT INTO users VALUES ('mary', 'Mary Hung', '99999999', 'HK', '{noop}marypw');
INSERT INTO user_roles(username, role) VALUES ('mary', 'ROLE_STUDENT');