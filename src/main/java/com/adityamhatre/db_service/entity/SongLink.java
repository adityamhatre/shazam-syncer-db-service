package com.adityamhatre.db_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Table(name = "song_link", uniqueConstraints = {@UniqueConstraint(columnNames = {"shazam_song_id"})})
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SongLink implements Serializable {
	@Id
	@Column(name = "shazam_song_id")
	private String shazamSongId;

	@Column(name = "link")
	private String link;

}

