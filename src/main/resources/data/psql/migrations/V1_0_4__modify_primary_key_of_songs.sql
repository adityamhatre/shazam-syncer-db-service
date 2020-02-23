ALTER TABLE public.songs
    drop constraint songs_pkey;

ALTER TABLE songs
    ALTER COLUMN shazamed_by TYPE varchar (1024);

ALTER TABLE songs
    ALTER COLUMN shazamed_by SET NOT NULL ;

ALTER TABLE songs
    ADD PRIMARY KEY (shazam_song_id, shazamed_by);