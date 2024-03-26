package com.moyeota.moyeotaproject.controller.dto.UsersDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersResponseDto {
	private Long id;
	private String loginId;
	private String name;
	private String nickName;
	private String profileImage;
	private String phoneNumber;
	private String email;
	private String status;
	private Float averageStarRate;
	private String school;
	private String age;
	private String gender;
}
