package com.moyeota.moyeotaproject.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedDate
	private LocalDateTime modifiedDate;

	@PrePersist
	public void prePersist() {
		LocalDateTime nowInKorea = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
		if (this.createdDate == null) {
			this.createdDate = nowInKorea;
		}
		if (this.modifiedDate == null) {
			this.modifiedDate = nowInKorea;
		}
	}

	@PreUpdate
	public void preUpdate() {
		this.modifiedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
	}
}
