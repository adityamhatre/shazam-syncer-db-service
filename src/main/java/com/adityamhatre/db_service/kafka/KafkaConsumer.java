package com.adityamhatre.db_service.kafka;

import com.adityamhatre.db_service.entity.Song;
import com.adityamhatre.db_service.entity.SongLink;
import com.adityamhatre.db_service.entity.UniqueSongId;
import com.adityamhatre.db_service.entity.User;
import com.adityamhatre.db_service.repository.SongLinkRepository;
import com.adityamhatre.db_service.repository.SongRepository;
import com.adityamhatre.db_service.repository.UserRepository;
import dto.SongDTO;
import dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.adityamhatre.db_service.kafka.Topics.TopicConstants.ON_FOUND_SONG_LINK;
import static com.adityamhatre.db_service.kafka.Topics.TopicConstants.ON_RECEIVE_NEW_SONG;
import static com.adityamhatre.db_service.kafka.Topics.TopicConstants.ON_RECEIVE_NEW_USER;
import static com.adityamhatre.db_service.kafka.Topics.TopicConstants.ON_USER_FETCHED_ALL_SONGS;

@Component
@Slf4j
public class KafkaConsumer {
	private final UserRepository userRepository;
	private final SongRepository songRepository;
	private final SongLinkRepository songLinkRepository;


	public KafkaConsumer(UserRepository userRepository, SongRepository songRepository, KafkaProducer kafkaProducer, SongLinkRepository songLinkRepository) {
		this.userRepository = userRepository;
		this.songRepository = songRepository;
		this.songLinkRepository = songLinkRepository;
	}

	@KafkaListener(topics = ON_RECEIVE_NEW_USER, groupId = "shazam.db", containerFactory = "kafkaListenerContainerFactory")
	void writeUser(ConsumerRecord<String, UserDTO> record) {
		log.info("Got new value on \"{}\" channel", ON_RECEIVE_NEW_USER);
		UserDTO userDTO = record.value();
		if (!this.userRepository.findById(userDTO.getObjectId()).isPresent()) {
			User user = User.builder()
					.objectId(userDTO.getObjectId())
					.inid(userDTO.getInid())
					.codever(userDTO.getCodever())
					.deviceFcmToken(userDTO.getDeviceFcmToken())
					.username(userDTO.getUsername())
					.frequency(userDTO.getFrequency())
					.build();

			this.userRepository.save(user);
			log.info("User: {} saved", user);
		} else {
			log.info("User with id {} already exists. Skipping both inserting and bootstrapping", userDTO.getObjectId());
		}
	}


	@KafkaListener(topics = ON_RECEIVE_NEW_SONG, groupId = "shazam.db", containerFactory = "kafkaListenerSongContainerFactory")
	void writeSong(ConsumerRecord<String, SongDTO> record) {
		log.info("Got new value on \"{}\" channel", ON_RECEIVE_NEW_SONG);
		SongDTO songDTO = record.value();

		UserDTO userDTO = songDTO.getShazamedBy();
		User user = User.builder()
				.objectId(userDTO.getObjectId())
				.inid(userDTO.getInid())
				.codever(userDTO.getCodever())
				.deviceFcmToken(userDTO.getDeviceFcmToken())
				.username(userDTO.getUsername())
				.build();


		Song song;
		Optional<Song> songRecord = this.songRepository.findById(new UniqueSongId(songDTO.getShazamSongId(), user));
		if (songRecord.isPresent()) {
			song = songRecord.get();
		} else {
			song = Song.builder().build();
			if (!this.userRepository.findById(user.getObjectId()).isPresent()) {
				this.userRepository.save(user);
			}
			song.setUniqueSong(new UniqueSongId(songDTO.getShazamSongId(), user));
		}
		song.setSongName(songDTO.getSongName());
		song.setTimestamp(songDTO.getTimestamp());

		log.info("Song: {} saved", song);
		this.songRepository.save(song);
	}

	@KafkaListener(topics = ON_USER_FETCHED_ALL_SONGS, groupId = "shazam.db", containerFactory = "kafkaListenerSongContainerFactory")
	void bootStrapUser(ConsumerRecord<String, SongDTO> record) {
		log.info("Got new value on \"{}\" channel", ON_USER_FETCHED_ALL_SONGS);
		UserDTO userDTO = record.value().getShazamedBy();
		User user;
		if (this.userRepository.findById(userDTO.getObjectId()).isPresent()) {
			user = this.userRepository.findById(userDTO.getObjectId()).get();
			user.setBootStrapped(userDTO.isBootStrapped());
		} else {
			log.error("User with object Id \"{}\" not found, Creating one now...", userDTO.getObjectId());
			user = User.builder()
					.objectId(userDTO.getObjectId())
					.inid(userDTO.getInid())
					.codever(userDTO.getCodever())
					.deviceFcmToken(userDTO.getDeviceFcmToken())
					.username(userDTO.getUsername())
					.frequency(userDTO.getFrequency())
					.bootStrapped(userDTO.isBootStrapped())
					.build();

		}
		this.userRepository.save(user);
		log.info("User: {} bootstrapped", user);
	}

	@KafkaListener(topics = ON_FOUND_SONG_LINK, groupId = "shazam.db", containerFactory = "kafkaListenerSongContainerFactory")
	void updateSongLink(ConsumerRecord<String, SongDTO> record) {
		log.info("Got new value on \"{}\" channel", ON_FOUND_SONG_LINK);
		SongDTO songDTO = record.value();
		List<Song> songs = this.songRepository.findAllByUniqueSong_shazamSongId(songDTO.getShazamSongId());
		songs.forEach(song -> {
			this.songLinkRepository.save(SongLink.builder().link(songDTO.getLink()).shazamSongId(song.getUniqueSong().getShazamSongId()).build());
			log.trace("Song {} updated with link", song.getSongName());
		});
		if (songs.isEmpty()) {
			Song song = Song.builder()
					.songName(songDTO.getSongName())
					.timestamp(songDTO.getTimestamp())
					.uniqueSong(new UniqueSongId(songDTO.getShazamSongId(), User.builder()
							.objectId(songDTO.getShazamedBy().getObjectId())
							.bootStrapped(songDTO.getShazamedBy().isBootStrapped())
							.inid(songDTO.getShazamedBy().getInid())
							.codever(songDTO.getShazamedBy().getCodever())
							.deviceFcmToken(songDTO.getShazamedBy().getDeviceFcmToken())
							.frequency(songDTO.getShazamedBy().getFrequency())
							.build()))
					.build();
			this.songRepository.save(song);
			this.songLinkRepository.save(SongLink.builder().link(songDTO.getLink()).shazamSongId(song.getUniqueSong().getShazamSongId()).build());

		}
	}
}
