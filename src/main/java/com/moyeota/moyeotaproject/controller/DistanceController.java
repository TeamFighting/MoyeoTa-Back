package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.service.AddressSearchService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

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
    public ResponseDto getDistance(@RequestParam String address) {
        return ResponseUtil.SUCCESS("위도 경도 계산 완료", addressSearchService.requestAddressSearch(address));
    }

    @ApiOperation(value = "주소 입력 거리 계산", notes = "주소를 입력받으면 거리 계산해주는 API")
    @GetMapping("/compare")
    public ResponseDto compareDistance(@RequestParam String address1, @RequestParam String address2) {
        return ResponseUtil.SUCCESS("거리 계산 완료", addressSearchService.compareAddress(address1, address2));
    }

    //출발지에서 목적지까지 예상 이동시간 및 금액
    @ApiOperation(value = "예상 이동시간(초) 및 금액(원) 조회", notes = "출발지에서 목적지까지의 예상 이동시간 및 금액을 조회하는 API. 출발지와 도착지에 주소 입력 ex) origin=광나루로 458&destination=송파구 위례")
    @GetMapping("/duration/fare")
    public ResponseDto getDurationAndFare(@RequestParam String origin, @RequestParam String destination) throws ParseException {
        return ResponseUtil.SUCCESS("이동 시간 및 금액 조회에 성공하였습니다.", addressSearchService.getDurationAndFare(origin, destination));
    }

}
