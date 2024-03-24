package com.moyeota.moyeotaproject.domain.participationdetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.users.Users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class ParticipationDetails extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ParticipationDetailsStatus status;

	private double distance;

	private double price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private Users user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "postId")
	private Posts post;

	//연관관계 메서드
	public void setUser(Users user) {
		this.user = user;
		user.getParticipationDetails().add(this);
	}

	public void setPost(Posts post) {
		this.post = post;
		post.getParticipationDetails().add(this);
	}

	public void updateDistance(double distance) {
		this.distance = distance;
	}

	public void updatePrice(double price) {
		this.price = price;
	}

	@Builder
	public ParticipationDetails(Users user, Posts post) {
		this.status = ParticipationDetailsStatus.JOIN;
		this.setUser(user);
		this.setPost(post);
	}
	/**
	 public boolean cancel() {
	 if (this.status == ParticipationDetailsStatus.CANCEL) {
	 return true;
	 }
	 this.status = ParticipationDetailsStatus.CANCEL;
	 return false;
	 }
	 */
}
