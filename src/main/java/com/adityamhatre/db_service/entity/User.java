package com.adityamhatre.db_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Table(name = "users", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"object_id"})
})
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@ToString
public class User implements Serializable {
	@Id
	@Column(name = "object_id")
	private String objectId;

	@Column(name = "inid")
	private String inid;

	@Column(name = "codever")
	private String codever;

	@Column(name = "username")
	private String username;

	@Column(name = "fcm_token")
	private String deviceFcmToken;

	@Column(name = "frequency")
	private Integer frequency;

	@Column(name = "boot_strapped")
	private Boolean bootStrapped;
}
