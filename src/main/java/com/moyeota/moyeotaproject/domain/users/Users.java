package com.moyeota.moyeotaproject.domain.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.*;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.domain.account.Account;
import com.moyeota.moyeotaproject.dto.UsersDto.UserDto;
import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.chatroomandusers.ChatRoomAndUsers;
import com.moyeota.moyeotaproject.domain.oAuth.OAuth;
import com.moyeota.moyeotaproject.domain.participationdetails.ParticipationDetails;
import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.review.Review;

import io.swagger.annotations.Api;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Users extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String profileImage;

	private String nickName;

	private String phoneNumber;

	private String email;

	@Column(nullable = false)
	private String loginId; // OAuthProvider + email
	@Column(nullable = false)
	private String password;
	private String status;
	private String gender;
	private String age;
	private Float averageStarRate;
	private String school;
	private Boolean isAuthenticated;

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<OAuth> oAuths = new ArrayList<>();

	@Getter
	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Posts> posts = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Review> reviews = new ArrayList<>();

	@OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ParticipationDetails> participationDetails = new ArrayList<>();

	//    @OneToMany(mappedBy = "user", orphanRemoval = true)
	//    private List<ChatMessage> chatMessages = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<ChatRoomAndUsers> chatRoomAndUsersList = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Account> accountList = new ArrayList<>();

	public void addPost(Posts post) {
		posts.add(post);
		post.setUser(this);
	}

	public void updateOAuth(OAuth oAuth) {
		oAuths.add(oAuth);
	}

	// 프로필 정보 전체 업데이트
	public void updateUsers(UserDto.updateDto usersDto) {
		this.profileImage = Optional.ofNullable(usersDto.getProfileImage()).orElse(this.profileImage);
		this.phoneNumber = Optional.ofNullable(usersDto.getPhoneNumber()).orElse(this.phoneNumber);
		this.age = Optional.ofNullable(usersDto.getAge()).orElse(this.age);
		this.email = Optional.ofNullable(usersDto.getEmail()).orElse(this.email);
		this.gender = Optional.ofNullable(usersDto.getGender()).orElse(this.gender);
	}

	@Builder
	public Users(Long id, String name, String nickName, String profileImage, String phoneNumber, String email, String loginId,
				 String password, String status, String gender, Float averageStarRate, String school, Boolean isAuthenticated,
				 String age) {
		this.id = id;
		this.name = name;
		this.nickName = nickName;
		this.profileImage = profileImage;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.loginId = loginId;
		this.password = password;
		this.status = status;
		this.gender = gender;
		this.averageStarRate = averageStarRate;
		this.school = school;
		this.isAuthenticated = isAuthenticated;
		this.age = age;
	}

	public void setDefaultProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void updateProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

	public void setNickName(String nickName) {
		if (nickName == null || nickName.isEmpty()) {
			throw new ApiException(ErrorCode.INPUT_ERROR);
		}
		this.nickName = nickName;
	}

	public void updateSchoolAuthenticate(String univName) {
		this.school = univName;
		this.isAuthenticated = Boolean.TRUE;
	}

	public void addAccount(Account account) {
		accountList.add(account);
		account.setUser(this);
	}

	public void updatePhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
