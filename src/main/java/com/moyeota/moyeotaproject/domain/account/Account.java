package com.moyeota.moyeotaproject.domain.account;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.moyeota.moyeotaproject.domain.users.Users;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String bankName;

	private String accountNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private Users user;

	@Builder
	public Account(String bankName, String accountNumber) {
		this.bankName = bankName;
		this.accountNumber = accountNumber;
	}

	public void setUser(Users user) {
		this.user = user;
		user.getAccountList().add(this);
	}
}
