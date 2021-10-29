create table if not exists assignee
(
    id uuid primary key default gen_random_uuid(),
    name varchar(64)
);

create table if not exists task
(
    id uuid primary key default gen_random_uuid(),
    description varchar(1024),
    assignee uuid references assignee(id),
    status varchar(16) default 'IN_PROGRESS'
);
