USE rbac;

INSERT INTO rbac.user(user_id, username, password ) VALUES ( 1, 'hikari', 'desilva6' );

COMMIT;


SELECT * FROM rbac.user WHERE username = 'hikari';