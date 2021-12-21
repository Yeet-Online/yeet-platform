create sequence yeets_id start with 1 increment by 1;

create table yeets (
    id bigint,
    user_id bigint,
    content varchar(250) not null,
    likes bigint,
    date_created datetime,
    date_updated datetime,
    primary key (id),
    foreign key (user_id) references users(id)
);
