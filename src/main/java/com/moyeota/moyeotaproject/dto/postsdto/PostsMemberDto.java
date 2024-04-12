package com.moyeota.moyeotaproject.dto.postsdto;

import com.moyeota.moyeotaproject.domain.account.Account;
import com.moyeota.moyeotaproject.domain.users.Users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "모집글 멤버 목록 조회 응답")
public class PostsMemberDto {

	@ApiModelProperty(value = "유저 인덱스")
	private Long userId;
	@ApiModelProperty(value = "유저 이름")
	private String nickname;
	@ApiModelProperty(value = "프로필 이미지")
	private String profileImage;
	@ApiModelProperty(value = "유저 성별")
	private String userGender;
	@ApiModelProperty(value = "팟장 여부")
	private boolean isPotOwner;
	@ApiModelProperty(value = "은행명")
	private String bankName;
	@ApiModelProperty(value = "계좌번호")
	private String accountNumber;

	@Builder
	public PostsMemberDto(Users user, String nickname, boolean isPotOwner, String bankName, String accountNumber) {
		this.userId = user.getId();
		this.nickname = nickname;
		this.profileImage = user.getProfileImage();
		this.userGender = user.getGender();
		this.isPotOwner = isPotOwner;
		this.bankName = bankName;
		this.accountNumber = accountNumber;
	}

}
