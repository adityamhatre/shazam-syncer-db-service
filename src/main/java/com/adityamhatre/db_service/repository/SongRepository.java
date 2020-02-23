package com.adityamhatre.db_service.repository;

import com.adityamhatre.db_service.entity.Song;
import com.adityamhatre.db_service.entity.UniqueSongId;
import com.adityamhatre.db_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, UniqueSongId> {
	List<Song> findAllByUniqueSong_shazamSongId(String shazamSongId);
	Song findFirstByUniqueSongShazamedByOrderByTimestampDesc(User user);
}
