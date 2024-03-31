package com.moyeota.moyeotaproject.dto.KakaoApiDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDto {

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("x")
    private double longitude;

    // 구현하다가 필요하면 아래 정보도 이후에 추가
//    @JsonProperty("address")
//    private Address address;

//    @JsonProperty("road_address")
//    private RoadAddress roadAddress;
}
