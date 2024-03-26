package com.moyeota.moyeotaproject.controller.dto.KakaoApiDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "택시 요금 및 이동 시간 응답")
public class DurationAndFareResponseDto {

	@ApiModelProperty(value = "택시 요금", example = "5200")
	private int fare;

	@ApiModelProperty(value = "이동 시간(초)", example = "313")
	private int duration;

}
