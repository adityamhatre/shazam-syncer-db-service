create table if not exists song_link
(
    shazam_song_id varchar(255) primary key,
    link           varchar(255)
);

insert into song_link
select shazam_song_id, link
from songs;

alter table songs
    drop column link;

alter table songs
    drop column link_found;