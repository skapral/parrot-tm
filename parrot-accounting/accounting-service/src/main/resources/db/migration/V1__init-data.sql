create table if not exists account
(
    id uuid primary key,
    value integer
);

create table if not exists transactionlog
(
    id uuid primary key,
    time timestamp default 'now',
    accountid uuid,
    description varchar(256),
    value integer
);

create table if not exists reward
(
    taskid uuid primary key,
    reward integer,
    penalty integer
)
