delete from user_role;
delete from usr;

insert into usr(id, active, password, username) values
(1, true, '$2a$08$xpCxrFh/RpqOZrK5fqt7VOLggrJ3fFNxMlH.rUzwXcA8dM82Q.ts.', 'admin'),
(2, true, '$2a$08$xpCxrFh/RpqOZrK5fqt7VOLggrJ3fFNxMlH.rUzwXcA8dM82Q.ts.', 'mike');

insert into user_role(user_id, roles) values
(1, 'USER'), (1, 'ADMIN'),
(2, 'USER');