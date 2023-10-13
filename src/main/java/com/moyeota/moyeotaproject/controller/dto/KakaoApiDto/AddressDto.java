package com.moyeota.moyeotaproject.controller.dto.KakaoApiDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "주소 응답")
public class AddressDto {

    @ApiModelProperty(value = "장소명")
    private String place_name;

    @ApiModelProperty(value = "전체 지번 주소")
    private String address_name;

    @ApiModelProperty(value = "전체 도로명 주소")
    private String road_address_name;

    @ApiModelProperty(value = "X 좌표값(경도)")
    private String x;

    @ApiModelProperty(value = "Y 좌표값(위도)")
    private String y;

}
