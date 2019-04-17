delete from message;
insert into message(id, text, tag, user_id, downloads) values
(1, 'first', 'my-tag', 1, 0),
(2, 'second', 'more', 1, 0),
(3, 'third', 'my-tag', 1, 0),
(4, 'fourth', 'another', 1, 0);

alter sequence hibernate_sequence restart with 10;