package com.moyeota.moyeotaproject.domain.location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Location {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String sessionId;

	private String position;

	@Builder
	public Location(String sessionId, String position) {
		this.sessionId = sessionId;
		this.position = position;
	}

}
