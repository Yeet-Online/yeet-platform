create sequence users_id start with 1 increment by 1;

create table users (
    id bigint,
    username varchar(255) not null,
    password_hash varchar(255) not null,
    date_created date,
    date_updated date,
    primary key (id)
);
