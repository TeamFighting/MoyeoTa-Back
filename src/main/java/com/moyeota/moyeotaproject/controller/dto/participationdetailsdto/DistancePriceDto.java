package com.moyeota.moyeotaproject.controller.dto.participationdetailsdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "이동거리 및 금액 조회 응답")
public class DistancePriceDto {

	@ApiModelProperty(value = "이동거리")
	private double distance;
	@ApiModelProperty(value = "금액")
	private double price;

	@Builder
	public DistancePriceDto(double distance, double price) {
		this.distance = distance;
		this.price = price;
	}
}
