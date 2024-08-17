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
import com.moyeota.moyeotaproject.dto.locationdto.LocationDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "Location", description = "Location Controller")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/location")
public class LocationController {

	private final LocationRepository locationRepository;

	@ApiOperation(value = "경로 찾기")
	@GetMapping("/users/{userId}/posts/{postId}")
	public ResponseDto<List<LocationDto>> findAllBySessionIdDesc(@PathVariable Long userId, @PathVariable Long postId) {
		List<LocationDto> positionList = locationRepository.findAllByUserAndPost(userId, postId);
		return ResponseUtil.SUCCESS("경로 조회에 성공하였습니다.", positionList);
	}

}
