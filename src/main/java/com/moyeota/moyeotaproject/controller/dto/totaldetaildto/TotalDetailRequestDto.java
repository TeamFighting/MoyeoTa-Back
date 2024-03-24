package com.moyeota.moyeotaproject.controller.dto.totaldetaildto;

import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.totaldetail.TotalDetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "최종 거리 및 요금 입력 요청")
public class TotalDetailRequestDto {

	@ApiModelProperty(value = "최종 도착 거리(km)", example = "9.8", required = true)
	private double totalDistance;

	@ApiModelProperty(value = "최종 도착 금액(원)", example = "13600", required = true)
	private double totalPayment;

	public TotalDetail toEntity(Posts post) {
		return TotalDetail.builder()
			.totalDistance(totalDistance)
			.totalPayment(totalPayment)
			.post(post)
			.build();
	}

}
