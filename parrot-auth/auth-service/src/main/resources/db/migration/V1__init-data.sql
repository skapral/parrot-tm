create sequence if not exists userseq as integer start with 100 increment by 1;

create table if not exists "user"
(
    id integer default nextval('userseq') primary key,
    login varchar(64) unique,
    role varchar(16)
);

insert into "user" (id, login, role) values (1, 'innokentiy', 'MANAGER');
insert into "user" (id, login, role) values (2, 'jorik', 'ADMIN');
