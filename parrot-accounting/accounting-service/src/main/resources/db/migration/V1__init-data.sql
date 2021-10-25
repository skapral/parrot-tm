create table if not exists account
(
    id integer primary key,
    value integer
);

create table if not exists transactionlog
(
    id integer primary key,
    time timestamp default 'now',
    accountid integer,
    description varchar(256),
    value integer
);

create table if not exists reward
(
    taskid integer primary key,
    reward integer,
    penalty integer
)
