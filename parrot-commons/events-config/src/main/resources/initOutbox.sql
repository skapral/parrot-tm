create table if not exists "outbox"
(
    id uuid default gen_random_uuid() primary key,
    sent boolean default false,
    type varchar(16),
    outbox varchar(32),
    routingKey varchar(32),
    payload text
);
