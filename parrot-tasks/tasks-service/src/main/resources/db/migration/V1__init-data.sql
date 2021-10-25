create sequence if not exists assigneeseq as integer start with 100 increment by 1;

create table if not exists assignee
(
    id integer primary key default nextval('assigneeseq'),
    name varchar(64)
);

create sequence if not exists taskseq as integer start with 100 increment by 1;

create table if not exists task
(
    id integer primary key default nextval('taskseq'),
    description varchar(1024),
    assignee integer references assignee(id),
    status varchar(16)
);
