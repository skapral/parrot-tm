create table if not exists account
(
    id uuid primary key,
    money integer not null default 0
);

create table if not exists transactionlog
(
    id uuid primary key,
    time timestamp default 'now' not null,
    accountid uuid not null,
    description varchar(256),
    debit integer,
    credit integer
);

create table if not exists taskcost
(
    taskid uuid primary key,
    reward integer not null,
    penalty integer not null
)
