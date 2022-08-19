create table if not exists users
(
    id  long AUTO_INCREMENT NOT NULL,
    login    varchar(500),
    name     varchar(50),
    email    varchar(50),
    birthday date,
    CONSTRAINT pk_users PRIMARY KEY (id)
);
insert into USERS (ID, LOGIN, NAME, EMAIL, BIRTHDAY) VALUES (1, 'Kitty', 'Mary', 'kit@ya', '2000-09-09' )
delete from USERS;
drop table users;

insert into rating (rating_id, RATING_NAME)
values (1, 'G');
insert into rating (rating_id, RATING_NAME)
values (2, 'PG');
insert into rating (rating_id, RATING_NAME)
values (3, 'PG-13');
insert into rating (rating_id, RATING_NAME)
values (4, 'R');
insert into rating (rating_id, RATING_NAME)
values (5, 'NC-17');

insert into genres (id, genre_name)
values (1, 'Комедия');
insert into genres (id, genre_name)
values (2, 'Драма');
insert into genres (id, genre_name)
values (3, 'Мультфильм');
insert into genres (id, genre_name)
values (4, 'Триллер');
insert into genres (id, genre_name)
values (5, 'Документальный');
insert into genres (id, genre_name)
values (6, 'Боевик');