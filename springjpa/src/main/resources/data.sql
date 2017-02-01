INSERT INTO user(username,password,email,enabled, created_by, created_date_time, updated_by, updated_date_time)
VALUES ('admin','$2a$10$LYMs2dw2l9h9SCaI/uUDjO.kukhP1FwAxpgYoLc2a67N9dUeBUGMO', null, true, 1, now(),1, now());

INSERT INTO role(name,description, created_by, created_date_time, updated_by, updated_date_time)
VALUES ('ROLE_ADMIN',null, 1, now(),1, now());
INSERT INTO role(name,description, created_by, created_date_time, updated_by, updated_date_time)
VALUES ('ROLE_USER',null, 1, now(),1, now());

INSERT INTO user_roles(user_id,role_id)
select user.id,role.id from user left join role on role.name='ROLE_USER' where user.username='admin' ;
INSERT INTO user_roles(user_id,role_id)
select user.id,role.id from user left join role on role.name='ROLE_ADMIN' where user.username='admin' ;