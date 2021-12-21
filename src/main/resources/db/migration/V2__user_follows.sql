create table user_follows (
    source_user_id bigint,
    target_user_id bigint,
    date_created datetime default now(),
    primary key (source_user_id, target_user_id),
    foreign key (source_user_id) references users(id),
    foreign key (target_user_id) references users(id),
    check (source_user_id <> target_user_id)
);
