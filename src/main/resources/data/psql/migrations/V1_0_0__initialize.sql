create table if not exists users
(
    object_id varchar(255) primary key,
    inid      varchar(1024),
    codever   varchar(1024),
    fcm_token varchar(1024),
    username  varchar(1024)
);

create table if not exists songs
(
    shazam_song_id      varchar(255) primary key,
    song_name      varchar(255),
    link_found     boolean,
    link           varchar(1024),
    shazamed_by    varchar(1024),
    constraint fk_songs_user
        foreign key (shazamed_by)
            references users (object_id)
);
