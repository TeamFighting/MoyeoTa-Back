package com.moyeota.moyeotaproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.domain.location.Location;
import com.moyeota.moyeotaproject.domain.location.LocationRepository;
import com.moyeota.moyeotaproject.dto.locationdto.LocationGetResponseDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = "Location", description = "Location Controller")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/location")
public class LocationController {

	private final LocationRepository locationRepository;

	@ApiOperation(value = "경로 찾기")
	@GetMapping("/users/{userId}")
	public ResponseDto<List<LocationGetResponseDto>> findAllBySessionIdDesc(@ApiParam(value = "유저 인덱스") @PathVariable("userId") Long userId) {
		List<Location> positionList = locationRepository.findAllByUserId(String.valueOf(userId));
		List<LocationGetResponseDto> locationGetResponseDtoList = new ArrayList<>();
		for (int i=0; i<positionList.size(); i++) {
			locationGetResponseDtoList.add(LocationGetResponseDto.builder().position(positionList.get(i).getPosition()).build());
		}
		return ResponseUtil.SUCCESS("경로 조회에 성공하였습니다.", locationGetResponseDtoList);
	}

}
