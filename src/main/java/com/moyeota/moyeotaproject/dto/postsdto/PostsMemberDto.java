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
	private String userName;
	@ApiModelProperty(value = "프로필 이미지")
	private String profileImage;
	@ApiModelProperty(value = "유저 성별")
	private String userGender;

	@Builder
	public PostsMemberDto(Users user) {
		this.userName = user.getName();
		this.profileImage = user.getProfileImage();
		this.userGender = user.getGender();
	}

}
