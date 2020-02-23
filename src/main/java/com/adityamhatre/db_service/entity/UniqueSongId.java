package com.adityamhatre.db_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UniqueSongId implements Serializable {
	@Column(name = "shazam_song_id")
	private String shazamSongId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "shazamed_by", referencedColumnName = "object_id")
	private User shazamedBy;

}
