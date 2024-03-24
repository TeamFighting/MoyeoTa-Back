package com.moyeota.moyeotaproject.controller.dto.UsersDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsersUpdateDto {
	private String name;
	private String profileImage;
	private String phoneNumber;
	private String age;
	private String email;
	private String gender;
}
