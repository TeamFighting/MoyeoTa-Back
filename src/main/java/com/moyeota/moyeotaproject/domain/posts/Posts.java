package com.moyeota.moyeotaproject.domain.posts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicUpdate;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.chatroom.ChatRoom;
import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.dto.postsdto.PostsUpdateRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@DynamicUpdate
@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Enumerated(EnumType.STRING)
	private Category category;

	@Column(nullable = false)
	private String departure;

	@Column(nullable = false)
	private String destination;

	@Column(nullable = false)
	private LocalDateTime departureTime;

	private String content;

	@Enumerated(EnumType.STRING)
	private SameGender sameGenderStatus;

	@Enumerated(EnumType.STRING)
	private Vehicle vehicle;

	@Column(nullable = false)
	private int numberOfRecruitment;

	@Column(nullable = false)
	private int numberOfParticipants;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PostsStatus status;

	@Column(nullable = false)
	private int fare;

	@Column(nullable = false)
	private int duration;

	@Column(nullable = false)
	private double distance;

	@Column(nullable = false)
	private int view;

	@Column(nullable = false)
	private String latitude;

	@Column(nullable = false)
	private String longitude;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId") //FK
	private Users user;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<ParticipationDetails> participationDetails = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "chatRoomId")
	private ChatRoom chatRoom;

	//연관관계 메서드
	public void setUser(Users user) {
		this.user = user;
		user.getPosts().add(this);
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	@Builder
	public Posts(String title, Category category, String departure, String destination, LocalDateTime departureTime,
		String content, SameGender sameGenderStatus, Vehicle vehicle, int numberOfRecruitment,
		int numberOfParticipants, int fare, int duration, double distance, Users user, ChatRoom chatRoom, String latitude, String longitude) {
		this.title = title;
		this.category = category;
		this.departure = departure;
		this.destination = destination;
		this.departureTime = departureTime;
		this.content = content;
		this.sameGenderStatus = sameGenderStatus;
		this.vehicle = vehicle;
		this.numberOfRecruitment = numberOfRecruitment;
		this.numberOfParticipants = numberOfParticipants;
		this.status = PostsStatus.RECRUITING;
		this.fare = fare;
		this.duration = duration;
		this.distance = distance;
		this.view = 0;
		this.latitude = latitude;
		this.longitude = longitude;
		this.setUser(user);
		this.setChatRoom(chatRoom);
	}

	public void addUser() {
		this.numberOfParticipants++;
	}

	public void minusUser() {
		this.numberOfParticipants--;
		this.status = PostsStatus.RECRUITING;
	}

	public void update(PostsUpdateRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
		this.category = requestDto.getCategory();
		this.departure = requestDto.getDeparture();
		this.destination = requestDto.getDestination();
		this.departureTime = requestDto.getDepartureTime();
		this.sameGenderStatus = requestDto.getSameGenderStatus();
		this.vehicle = requestDto.getVehicle();
		this.numberOfRecruitment = requestDto.getNumberOfRecruitment();
		this.fare = requestDto.getFare();
		this.duration = requestDto.getDuration();
		this.distance = requestDto.getDistance();
		this.latitude = requestDto.getLatitude();
		this.longitude = requestDto.getLongitude();
	}

	public void postsComplete() {
		if (this.status == PostsStatus.COMPLETE) {
			throw new IllegalStateException("이미 모집이 마감된 글입니다.");
		}
		this.status = PostsStatus.COMPLETE;
	}
}
