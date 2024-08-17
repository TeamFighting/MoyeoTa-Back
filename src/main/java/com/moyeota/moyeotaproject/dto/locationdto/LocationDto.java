package com.moyeota.moyeotaproject.dto.locationdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel(value = "경로 조회 응답")
@Builder
@Getter
public class LocationDto {

	@ApiModelProperty(value = "위치")
	private String position;

}
