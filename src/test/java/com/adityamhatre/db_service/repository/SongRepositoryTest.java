package com.adityamhatre.db_service.repository;

import com.adityamhatre.db_service.entity.Song;
import com.adityamhatre.db_service.entity.UniqueSongId;
import com.adityamhatre.db_service.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@ActiveProfiles("memoryDatabaseProfile")
@DataJpaTest
@Slf4j
class SongRepositoryTest {
	@Autowired
	SongRepository songRepository;

	@Autowired
	UserRepository userRepository;

	@Test
	void testAddingDuplicateSongBySameUser() {

		User user1 = User.builder()
				.objectId("user1")
				.build();

		User user2 = User.builder()
				.objectId("user2")
				.build();

		this.userRepository.save(user1);
		this.userRepository.save(user2);

		Song song1user1 = Song.builder()
				.timestamp(111)
				.songName("song1user1")
				.uniqueSong(new UniqueSongId("song1", user1))
				.build();

		Song song2user1 = Song.builder()
				.timestamp(222)
				.songName("song2user1")
				.uniqueSong(new UniqueSongId("song2", user1))
				.build();

		Song song1user2 = Song.builder()
				.timestamp(333)
				.songName("song1user2")
				.uniqueSong(new UniqueSongId("song1", user2))
				.build();


		this.songRepository.saveAndFlush(song1user1);
		this.songRepository.saveAndFlush(song2user1);
		this.songRepository.saveAndFlush(song1user2);
		song1user2.setTimestamp(444);
		this.songRepository.saveAndFlush(song1user2);

		this.songRepository.findAll().forEach(song -> {
			log.info("Song {} shazamed by {} at {}", song.getUniqueSong().getShazamSongId(), song.getUniqueSong().getShazamedBy().getObjectId(), song.getTimestamp());
		});

	}

	@Test
	void testAddNewSong() {

		User user1 = User.builder().objectId("user1").build();
		User user2 = User.builder().objectId("user2").build();

		this.userRepository.save(user1);
		this.userRepository.save(user2);

		Song s1u1 = Song.builder().uniqueSong(new UniqueSongId("song1", user1)).build();
		Song s2u1 = Song.builder().uniqueSong(new UniqueSongId("song2", user1)).build();
		this.songRepository.save(s1u1);
		this.songRepository.save(s2u1);

		User user3 = User.builder().objectId("user3").build();
		Song s3u3 = Song.builder().uniqueSong(new UniqueSongId("song3", user3)).build();
		//if this user is not in db, create one
		if (!this.userRepository.findById(user3.getObjectId()).isPresent()) {
			this.userRepository.save(user3);
		}
		this.songRepository.save(s3u3);

		assertEquals(this.userRepository.findAll().size(), 3);
		assertEquals(this.songRepository.findAll().size(), 3);

	}
}