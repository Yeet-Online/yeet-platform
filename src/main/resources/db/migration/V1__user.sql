create table user (
    id bigint auto_increment,
    username varchar(255) not null,
    password_hash varchar(255) not null,
    date_created date,
    date_updated date,
    primary key (id)
);