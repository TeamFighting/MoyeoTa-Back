package com.moyeota.moyeotaproject.dto.totaldetaildto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "최종 거리 및 요금 조회 응답")
public class TotalDetailResponseDto {

	@ApiModelProperty(value = "최종 도착 거리(km)")
	private double totalDistance;

	@ApiModelProperty(value = "최종 도착 금액(원)")
	private double totalPayment;

	@Builder
	public TotalDetailResponseDto(double totalDistance, double totalPayment) {
		this.totalDistance = totalDistance;
		this.totalPayment = totalPayment;
	}
}
