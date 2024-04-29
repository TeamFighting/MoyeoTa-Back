package com.moyeota.moyeotaproject.dto.locationdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "경로 조회 응답")
public class LocationGetResponseDto {

	@ApiModelProperty(value = "위치")
	private String position;

	@Builder
	public LocationGetResponseDto(String position) {
		this.position = position;
	}

}
