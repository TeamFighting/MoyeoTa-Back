package com.moyeota.moyeotaproject.controller;

import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.dto.KakaoApiDto.AddressDto;
import com.moyeota.moyeotaproject.dto.KakaoApiDto.DocumentDto;
import com.moyeota.moyeotaproject.dto.KakaoApiDto.DurationAndFareResponseDto;
import com.moyeota.moyeotaproject.service.AddressSearchService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Distance", description = "Distance Controller")
@ApiResponses({
	@ApiResponse(code = 200, message = "API 정상 작동"),
	@ApiResponse(code = 400, message = "BAD REQUEST"),
	@ApiResponse(code = 404, message = "NOT FOUND"),
	@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/distance")
public class DistanceController {

	private final AddressSearchService addressSearchService;

	@ApiOperation(value = "주소 입력 위도 경도 계산", notes = "주소를 입력받으면 위도 경도 계산해주는 API")
	@GetMapping("/info")
	public ResponseDto<DocumentDto> getDistance(@RequestParam String address) {
		return ResponseUtil.SUCCESS("위도 경도 계산 완료", addressSearchService.requestAddressSearch(address));
	}

	@ApiOperation(value = "주소 입력 거리 계산", notes = "주소를 입력받으면 거리 계산해주는 API")
	@GetMapping("/compare")
	public ResponseDto<String> compareDistance(@RequestParam String address1, @RequestParam String address2) {
		return ResponseUtil.SUCCESS("거리 계산 완료", addressSearchService.compareAddress(address1, address2));
	}

	@ApiOperation(value = "예상 이동시간(초) 및 금액(원) 조회",
		notes = "출발지에서 목적지까지의 예상 이동시간 및 금액을 조회하는 API. 출발지와 도착지에 주소 입력 ex) origin=광나루로 458&destination=송파구 위례")
	@GetMapping("/duration/fare")
	public ResponseDto<DurationAndFareResponseDto> getDurationAndFare(
		@ApiParam(value = "출발지 좌표") @RequestParam String origin,
		@ApiParam(value = "도착지 좌표") @RequestParam String destination) throws ParseException {
		return ResponseUtil.SUCCESS("이동 시간 및 금액 조회에 성공하였습니다.",
			addressSearchService.getDurationAndFare(origin, destination));
	}

	@ApiOperation(value = "키워드로 도로명주소 찾기", notes = "공릉역 7호선 입력시 서울특별시 노원구 동일로 지하 1074가 조회됨")
	@GetMapping("/keyword")
	public ResponseDto<AddressDto> getAddress(@ApiParam(value = "키워드") @RequestParam String query) throws
		ParseException {
		AddressDto addressDto = addressSearchService.findAddress(query);
		if (addressDto == null) {
			throw new IllegalArgumentException("일치하는 결과가 없습니다. keyword=" + query);
		}
		return ResponseUtil.SUCCESS("주소 검색 완료", addressDto);
	}

}
