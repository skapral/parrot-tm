create table if not exists "user"
(
    id uuid default gen_random_uuid() primary key,
    login varchar(64) unique,
    role varchar(16)
);

insert into "user" (login, role) values ('admin', 'ADMIN');
