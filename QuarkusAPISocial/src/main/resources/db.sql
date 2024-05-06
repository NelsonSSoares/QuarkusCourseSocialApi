create table followers(
                          id bigint not null primary key,
                          user_id bigint  null references user(id),
                          follower_id bigint not null references user(id)
);