package com.moyeota.moyeotaproject.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseUtil;
import com.moyeota.moyeotaproject.dto.UsersDto.AccountDto;
import com.moyeota.moyeotaproject.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(tags = "Account", description = "Account Controller")
@ApiResponses({
	@ApiResponse(code = 200, message = "API 정상 작동"),
	@ApiResponse(code = 400, message = "BAD REQUEST"),
	@ApiResponse(code = 404, message = "NOT FOUND"),
	@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

	private final AccountService accountService;

	@ApiOperation(value = "계좌 조회", notes = "특정 회원의 전체 계좌 조회 API(jwt토큰 필요)")
	@GetMapping("")
	public ResponseDto<List<AccountDto>> getAccount(HttpServletRequest request) {
		System.out.println("11111111111111");
		System.out.println(request.getHeader("Authorization"));
		return ResponseUtil.SUCCESS("계좌 조회에 성공하였습니다.", accountService.findAllByUser(request.getHeader("Authorization")));
	}

	@ApiOperation(value = "계좌 삭제", notes = "특정 회원의 계좌 삭제 API(jwt 토큰 필요)")
	@DeleteMapping("/{accountId}")
	public ResponseDto<String> deleteAccount(HttpServletRequest request, Long accountId) {
		return ResponseUtil.SUCCESS("계좌 삭제에 성공하였습니다.", accountService.delete(request.getHeader("Authorization"), accountId));
	}

}
