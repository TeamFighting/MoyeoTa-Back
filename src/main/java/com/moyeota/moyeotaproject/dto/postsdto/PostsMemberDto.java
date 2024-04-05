package com.moyeota.moyeotaproject.dto.postsdto;

import com.moyeota.moyeotaproject.domain.users.Users;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "모집글 멤버 목록 조회 응답")
public class PostsMemberDto {

	@ApiModelProperty(value = "유저 이름")
	private String nickname;
	@ApiModelProperty(value = "프로필 이미지")
	private String profileImage;
	@ApiModelProperty(value = "유저 성별")
	private String userGender;
	@ApiModelProperty(value = "팟장 여부")
	private boolean isPotOwner;

	@Builder
	public PostsMemberDto(Users user, String nickname, boolean isPotOwner) {
		this.nickname = nickname;
		this.profileImage = user.getProfileImage();
		this.userGender = user.getGender();
		this.isPotOwner = isPotOwner;
	}

}
