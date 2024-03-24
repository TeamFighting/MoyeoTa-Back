package com.moyeota.moyeotaproject.domain.oAuth;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.moyeota.moyeotaproject.domain.BaseTimeEntity;
import com.moyeota.moyeotaproject.domain.users.Users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OAuth extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String email;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private Users user;

	public void setUser(Users user) {
		this.user = user;
		user.getOAuths().add(this);
	}

	public void updateEmail(String email) {
		this.email = email;
	}

	@Builder
	public OAuth(String name, String email, Users user) {
		this.name = name;
		this.email = email;
		this.setUser(user);
	}
}
