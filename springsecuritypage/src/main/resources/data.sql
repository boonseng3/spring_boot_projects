INSERT INTO user(username,password,email,enabled, created_by, updated_by)
VALUES ('admin','$2a$10$LYMs2dw2l9h9SCaI/uUDjO.kukhP1FwAxpgYoLc2a67N9dUeBUGMO', null, true, 1, 1);
INSERT INTO user(username,password,email,enabled, created_by, updated_by)
VALUES ('user','$2a$10$LYMs2dw2l9h9SCaI/uUDjO.kukhP1FwAxpgYoLc2a67N9dUeBUGMO', 'user@example.com', true, 1, 1);

INSERT INTO role(name, description, created_by, updated_by)
VALUES ('ADMIN',null, 1, 1);
INSERT INTO role(name, description, created_by, updated_by)
VALUES ('USER',null, 1, 1);

INSERT INTO user_roles(user_id,role_id)
select user.id,role.id from user left join role on role.name='USER' where user.username='admin' ;
INSERT INTO user_roles(user_id,role_id)
select user.id,role.id from user left join role on role.name='ADMIN' where user.username='admin' ;