CREATE DATABASE RBAC;

CREATE TABLE RBAC.USER(
    user_id INTEGER AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(128) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL
);

INSERT INTO RBAC.`user`(username, `password`) VALUES ( 'fpmoles', 'z_mole');

INSERT INTO RBAC.`user`(username, `password`) VALUES ( 'jsmith', 'smith');


CREATE TABLE RBAC.AUTH_USER_GROUP (
  AUTH_USER_GROUP_ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  USERNAME VARCHAR(128) NOT NULL,
  AUTH_GROUP VARCHAR(128) NOT NULL,
  FOREIGN KEY(USERNAME) REFERENCES RBAC.USER(USERNAME),
  UNIQUE (USERNAME, AUTH_GROUP)
);

INSERT INTO RBAC.AUTH_USER_GROUP (USERNAME, AUTH_GROUP) VALUES('hikari', 'USER');
INSERT INTO RBAC.AUTH_USER_GROUP (USERNAME, AUTH_GROUP) VALUES('fpmoles', 'ADMIN');
INSERT INTO RBAC.AUTH_USER_GROUP (USERNAME, AUTH_GROUP) VALUES('jdoe', 'USER');
INSERT INTO RBAC.AUTH_USER_GROUP (USERNAME, AUTH_GROUP) VALUES('jsmith', 'PREMIUM');


COMMIT;


SELECT * FROM rbac.auth_user_group;



