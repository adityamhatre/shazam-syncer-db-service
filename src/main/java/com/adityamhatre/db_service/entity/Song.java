package com.adityamhatre.db_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Table(name = "songs", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"shazam_song_id", "shazamed_by"})
})
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Song implements Serializable {
	@EmbeddedId
	private UniqueSongId uniqueSong;

	@Column(name = "song_name")
	private String songName;

	@Column(name = "timestamp")
	private long timestamp;
}

