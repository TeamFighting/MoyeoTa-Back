package com.moyeota.moyeotaproject.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moyeota.moyeotaproject.config.exception.ApiException;
import com.moyeota.moyeotaproject.config.exception.ErrorCode;
import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.dto.totaldetaildto.TotalDetailRequestDto;
import com.moyeota.moyeotaproject.dto.totaldetaildto.TotalDetailResponseDto;
import com.moyeota.moyeotaproject.service.TotalDetailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "TotalDetail", description = "TotalDetail Controller")
@ApiResponses({
	@ApiResponse(code = 200, message = "API 정상 작동"),
	@ApiResponse(code = 400, message = "BAD REQUEST"),
	@ApiResponse(code = 404, message = "NOT FOUND"),
	@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/totalDetails/")
public class TotalDetailController {

	private final TotalDetailService totalDetailService;

	@ApiOperation(value = "최종 도착 정보 입력", notes = "팟장이 최종 택시 이용 거리 및 금액 입력 API")
	@PostMapping("/{postId}")
	public ResponseDto<Long> save(HttpServletRequest request, @RequestBody TotalDetailRequestDto requestDto,
		@PathVariable("postId") Long postId) {
		if (requestDto.getTotalPayment() == 0) {
			throw new ApiException(ErrorCode.TOTAL_DETAIL_EMPTY_PAYMENT);
		}
		// if (requestDto.getTotalDistance() == 0) {
		// 	throw new ApiException(ErrorCode.TOTAL_DETAIL_EMPTY_DISTANCE);
		// } else if (requestDto.getTotalPayment() == 0) {
		// 	throw new ApiException(ErrorCode.TOTAL_DETAIL_EMPTY_PAYMENT);
		// }
		Long totalDetailId = totalDetailService.save(request.getHeader("Authorization"), requestDto, postId);
		return ResponseUtil.SUCCESS("최종 도착 정보 입력에 성공하였습니다.", totalDetailId);
	}

	@ApiOperation(value = "최종 도착 정보 조회")
	@GetMapping("/{totalDetailId}")
	public ResponseDto<TotalDetailResponseDto> findById(@PathVariable("totalDetailId") Long totalDetailId) {
		return ResponseUtil.SUCCESS("최종 도착 정보 조회에 성공하였습니다.", totalDetailService.findTotalDetailById(totalDetailId));
	}

}
