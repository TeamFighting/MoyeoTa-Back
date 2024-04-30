package com.moyeota.moyeotaproject.dto.postsdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "파티원 도착 위치 응답")
public class MembersLocationResponseDto {

	@ApiModelProperty(value = "파티장 여부")
	private boolean isOwner;
	@ApiModelProperty(value = "유저 인덱스")
	private Long userId;
	@ApiModelProperty(value = "마자막 위치")
	private String position;

	@Builder
	public MembersLocationResponseDto(boolean isOwner, Long userId, String position) {
		this.isOwner = isOwner;
		this.userId = userId;
		this.position = position;
	}
}
