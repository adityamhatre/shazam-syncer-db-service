package com.adityamhatre.db_service.rest;

import com.adityamhatre.db_service.entity.Song;
import com.adityamhatre.db_service.entity.User;
import com.adityamhatre.db_service.repository.SongLinkRepository;
import com.adityamhatre.db_service.repository.SongRepository;
import com.adityamhatre.db_service.repository.UserRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import dto.SongDTO;
import dto.UserDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DBRestController {

	private final UserRepository userRepository;
	private final SongRepository songRepository;
	private final SongLinkRepository songLinkRepository;

	public DBRestController(UserRepository userRepository, SongRepository songRepository, SongLinkRepository songLinkRepository) {
		this.userRepository = userRepository;
		this.songRepository = songRepository;
		this.songLinkRepository = songLinkRepository;
	}

	@GetMapping(value = "/latestSongsPerUser")
	List<SongDTO> getLatestSongsPerUser() {
		List<SongDTO> songDTOList = new ArrayList<>();

		List<User> bootStrappedUsers = this.userRepository.findUsersByBootStrapped(true);
		bootStrappedUsers.forEach(user -> {
			Song song = this.songRepository.findFirstByUniqueSongShazamedByOrderByTimestampDesc(user);
			songDTOList.add(SongDTO.builder()
					.shazamSongId(song.getUniqueSong().getShazamSongId())
					.shazamedBy(UserDTO.builder()
							.frequency(user.getFrequency())
							.objectId(user.getObjectId())
							.bootStrapped(user.getBootStrapped())
							.inid(user.getInid())
							.codever(user.getCodever())
							.deviceFcmToken(user.getDeviceFcmToken())
							.username(user.getUsername())
							.build())
					.timestamp(song.getTimestamp())
					.songName(song.getSongName())
					.build());
		});

		return songDTOList;
	}

	@GetMapping(value = "/users/{userObjectId}/exists")
	Boolean doesUserExist(@PathVariable String userObjectId) {
		return this.userRepository.findById(userObjectId).isPresent();
	}

	@GetMapping(value = "/songs/{songShazamId}/link")
	Map<String, Object> getSongLink(@PathVariable String songShazamId) {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("link_exists", this.songLinkRepository.findById(songShazamId).isPresent());
		this.songLinkRepository.findById(songShazamId).ifPresent(songLink -> responseMap.put("link", songLink.getLink()));
		if (!responseMap.containsKey("link")) {
			responseMap.put("link", null);
		}
		return responseMap;
	}


}
